package uz.pdp.service;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.pdp.TodoBot;
import uz.pdp.dao.MemberDao;
import uz.pdp.model.Member;
import uz.pdp.model.Todo;
import uz.pdp.model.enums.MemberState;
import uz.pdp.utils.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TelegramService {
    private static TelegramService instance;
    private final MemberDao memberDao = MemberDao.getInstance();
    private final ButtonMaker buttonMaker = ButtonMaker.getInstance();
    private final CacheService cacheService = CacheService.getInstance();
    private TodoBot bot;

    public static TelegramService getInstance() {
        if (instance == null) {
            instance = new TelegramService();
        }
        return instance;
    }

    public void sendWelcome(SendMessage sendMessage, Member session) {
        sendMessage.setText(ContantMessages.welcome_message.formatted(session.getFullName()));
        sendMessage.setReplyMarkup(buttonMaker.mainMenu());
        sendMessage(sendMessage);
    }


    public Member registerMemberAndGet(User from, String chatId) {
        Optional<Member> byChatId = memberDao.findByChatId(chatId);
        Member member;
        if (byChatId.isPresent()) {
            return byChatId.get();
        }
        member = new Member();
        member.setChatId(chatId);
        String lastName = ((from.getLastName() == null) ? "" : " " + from.getLastName());
        member.setFullName(from.getFirstName() + lastName);
        member.setTodos(Collections.emptyList());
        memberDao.save(member);

        return member;
    }

    public void sendTodoTitleMessage(SendMessage sendMessage) {
        String chatId = sendMessage.getChatId();
        sendMessage.setText(ConstantMessages.SEND_TODO_TITLE_MESSAGE);
        cacheService.putState(chatId, MemberState.SEND_TODO_TITLE);
        sendMessage(sendMessage);
    }

    private TodoBot getBotInstance() {
        if (bot == null) {
            bot = new TodoBot();
        }
        return bot;
    }

    public void sendTodoDescriptionMessage(SendMessage sendMessage, String todoTitle) {
        String chatId = sendMessage.getChatId();
        sendMessage.setText(ConstantMessages.SEND_TODO_DESCRIPTION_MESSAGE);
        Todo todo = new Todo();
        todo.setTitle(todoTitle);
        cacheService.putTodo(chatId, todo);
        cacheService.putState(chatId, MemberState.SEND_TODO_DESCRIPTION);
        sendMessage(sendMessage);
    }

    public void sendSuccessfullyMessage(SendMessage sendMessage, String description) {
        String chatId = sendMessage.getChatId();
        Todo tempTodo = cacheService.getTodo(chatId);
        tempTodo.setDescription(description);
        memberDao.addTodo(tempTodo, chatId);
        sendMessage.setText(ConstantMessages.SUCCESSFULLY_CREATED_TODO_MESSAGE);
        cacheService.deleteState(chatId);
        sendMessage(sendMessage);
    }

    private void sendMessage(SendMessage sendMessage) {
        bot = getBotInstance();
        bot.sendMessage(sendMessage);
    }

    public void sendTodos(SendMessage sendMessage, Member session) {
        String chatId = sendMessage.getChatId();
        List<Todo> todos = session.getTodos();

        todos.forEach(t -> {
            SendMessage todoInfo = new SendMessage();
            todoInfo.setChatId(chatId);
            todoInfo.setText(prepareTodoInfo(t));
            todoInfo.setReplyMarkup(buttonMaker.todoButtons(t));
            sendMessage(todoInfo);
        });
    }

    private @NonNull String prepareTodoInfo(Todo todoInfo) {
        StringBuilder info = new StringBuilder();

        info.append("<b>").append(todoInfo.getTitle()).append("</b>")
                .append("\n")
                .append("\n")
                .append("<b>Description: </b>").append(todoInfo.getDescription())
                .append("\n")
                .append("<b>Created at: </b>").append(Utils.dateFormat(todoInfo.getCreatedAt(), "dd-MM-yyyy HH:mm"))
                .append("\n")
                .append(todoInfo.isCompleted() ? "✅" : "🕐");

        return info.toString();
    }

    public void doneTodo(String doneData, Member session, CallbackQuery callbackQuery) {
        String todoId = doneData.replace(CallBackPrefix.DONE, "");
        List<Todo> todos = session.getTodos();

        for (Todo todo : todos) {
            if (todo.getId().equals(todoId)) {
                todo.setCompleted(true);
                todo.setUpdatedAt(LocalDateTime.now().toString());
                memberDao.save(session);
                editTodoInfo(todo, session.getChatId(), callbackQuery.getMessage().getMessageId());
                showAlert(callbackQuery);
            }
        }
    }

    private void showAlert(CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setText("Successfully completed!!! ✅");
        bot.answerCallbackQuery(answerCallbackQuery);
    }

    private void editTodoInfo(Todo todo, String chatId, Integer messageId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(prepareTodoInfo(todo));
        editMessageText.setReplyMarkup(buttonMaker.todoButtons(todo));
        bot.editMessage(editMessageText);
    }

    public void deleteTodo(String data, Member session, int messageId) {
        String todoId = data.replace(CallBackPrefix.DELETE, "");
        session.getTodos().removeIf(t -> t.getId().equals(todoId));
        memberDao.save(session);
        bot.deleteMessage(session.getChatId(), messageId);
    }
}
