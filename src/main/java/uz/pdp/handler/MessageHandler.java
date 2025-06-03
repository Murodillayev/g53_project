package uz.pdp.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.model.Member;
import uz.pdp.model.enums.MemberState;
import uz.pdp.service.TelegramService;
import uz.pdp.utils.ButtonText;
import uz.pdp.utils.CacheService;

public class MessageHandler {
    private static MessageHandler instance;
    private final TelegramService telegramService = TelegramService.getInstance();
    private final CacheService cacheService = CacheService.getInstance();

    public static MessageHandler getInstance() {
        if (instance == null) {
            instance = new MessageHandler();
        }
        return instance;
    }

    public void handle(Message message) {
        String text = message.getText();
        String chatId = message.getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        Member session = telegramService.registerMemberAndGet(message.getFrom(), chatId);

        if (text.equals("/start")) {
            telegramService.sendWelcome(sendMessage, session);

        } else if (text.equals(ButtonText.ADD)) {
            telegramService.sendTodoTitleMessage(sendMessage);

        } else if (cacheService.getState(chatId).equals(MemberState.SEND_TODO_TITLE)) {
            telegramService.sendTodoDescriptionMessage(sendMessage, text);

        } else if (cacheService.getState(chatId).equals(MemberState.SEND_TODO_DESCRIPTION)) {
            telegramService.sendSuccessfullyMessage(sendMessage, text);

        } else if (text.equals(ButtonText.TASKS)) {
            telegramService.sendTodos(sendMessage,session);

        }

    }
}
