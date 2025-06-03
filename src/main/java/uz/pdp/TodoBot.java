package uz.pdp;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.handler.CallBackHandler;
import uz.pdp.handler.MessageHandler;


public class TodoBot extends TelegramLongPollingBot {

    private final MessageHandler messageHandler = MessageHandler.getInstance();
    private final CallBackHandler callBackHandler = CallBackHandler.getInstance();

    public TodoBot() {
        super("8140486869:AAH9TT-XmhIvsRslXVbAVzDZtM6P8b-YBrQ");
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            messageHandler.handle(update.getMessage());

        } else if (update.hasCallbackQuery()) {

            callBackHandler.handle(update.getCallbackQuery());
        }

    }

    @Override
    public String getBotUsername() {
        return "parrot_pdp_bot";
    }


    public void sendMessage(SendMessage message) {
        try {
            message.enableHtml(true);
            execute(message);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMessage(String chatId, int messageId) {
        try {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId);
            deleteMessage.setMessageId(messageId);
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void editMessage(EditMessageText editMessageText) {
        try {
            editMessageText.enableHtml(true);
            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void answerCallbackQuery(AnswerCallbackQuery answerCallbackQuery) {
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
//    @Override
//    public String getBotToken() {
//        return "8140486869:AAH9TT-XmhIvsRslXVbAVzDZtM6P8b-YBrQ";
//    }
}
