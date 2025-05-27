package uz.pdp;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.handler.CallBackHandler;
import uz.pdp.handler.MessageHandler;

public class CurrencyBot extends TelegramLongPollingBot {

    private final MessageHandler messageHandler = MessageHandler.getInstance();
    private final CallBackHandler callBackHandler = CallBackHandler.getInstance();

    public CurrencyBot() {
        super("8140486869:AAHAJfIABnTh1VC2G10eTlPV7faLORnHlG4");
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            messageHandler.handle(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            callBackHandler.handle(update.getCallbackQuery());
        }

    }

    @Override
    public String getBotUsername() {
        return "parrot_pdp_bot";
    }


    public void sendMessage(SendMessage sendMessage) {
        try {
            sendMessage.enableHtml(true);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteMessage(DeleteMessage deleteMessage) {
        try {
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
}



