package uz.pdp;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.handler.MessageHandler;

public class WordGamerBot extends TelegramLongPollingBot {

    private final MessageHandler messageHandler = MessageHandler.getInstance();

    public WordGamerBot() {
        super("8140486869:AAHAJfIABnTh1VC2G10eTlPV7faLORnHlG4");
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            messageHandler.handle(update.getMessage());
        } else {
            // warn message
        }

    }

    @Override
    public String getBotUsername() {
        return "parrot_pdp_bot";
    }














    public void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }




}



