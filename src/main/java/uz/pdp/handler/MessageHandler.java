package uz.pdp.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.*;

public class MessageHandler {

    private static MessageHandler instance;
    private final MemberDao memberDao = new MemberDao();
    private final ButtonMaker buttonMaker = ButtonMaker.getInstance();

    private MessageHandler() {

    }

    public static MessageHandler getInstance() {
        if (instance == null) {
            instance = new MessageHandler();
        }
        return instance;
    }

    public void handle(Message message) {
        CurrencyBot bot = new CurrencyBot();
        String text = message.getText();
        String chatId = message.getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        Member sessionMember = memberDao.getOrCreate(chatId);


        if (text.equals("/start")) {
            sendMessage.setText(ConstMessage.WELCOME_MESSAGE.formatted(message.getFrom().getFirstName()));
            sendMessage.setReplyMarkup(buttonMaker.mainMenuButtons());

        } else if (text.equals(ButtonText.SETTINGS)) {
            sendMessage.setText(ButtonText.SETTINGS);
            sendMessage.setReplyMarkup(buttonMaker.settingsButton(sessionMember));

        }

        bot.sendMessage(sendMessage);
    }


}
