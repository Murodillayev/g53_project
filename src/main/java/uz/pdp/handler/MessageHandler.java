package uz.pdp.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.*;
import uz.pdp.dao.MemberDao;
import uz.pdp.model.entity.Member;
import uz.pdp.service.TelegramService;
import uz.pdp.utils.ButtonText;

public class MessageHandler {

    private final TelegramService service = TelegramService.getInstance();

    private static MessageHandler instance;
    private final MemberDao memberDao = new MemberDao();

    private MessageHandler() {

    }

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
        Member sessionMember = memberDao.getOrCreate(chatId);


        if (text.equals("/start")) {
            service.sendWelcome(sendMessage, sessionMember);

        } else if (text.equals(ButtonText.SETTINGS)) {
            service.sendSettings(sendMessage, sessionMember);

        } else if (text.replace(".", "").matches("\\d+")) {
            service.sendResult(text, sendMessage, sessionMember);
        }
    }


}
