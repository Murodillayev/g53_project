package uz.pdp.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.WordGamerBot;
import uz.pdp.dao.WordDao;

public class MessageHandler {

    private static MessageHandler instance;

    private final WordDao wordDao = WordDao.getInstance();

    private MessageHandler() {

    }

    public static MessageHandler getInstance() {
        if (instance == null) {
            instance = new MessageHandler();
        }
        return instance;
    }

    public void handle(Message message) {
        WordGamerBot bot = new WordGamerBot();

        String text = message.getText();
        String chatId = message.getChatId().toString();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);


        if (text.equalsIgnoreCase("/go")) {
            wordDao.resetWords();
            sendMessage.setText("Qani boshladi\n\nIstalgan so'z yubor");

        } else if (text.equalsIgnoreCase("/start")) {
            sendMessage.setText("O'yinni boshlash uchun /go ni yubor");

        } else if (text.equalsIgnoreCase("/end")) {
            wordDao.refreshFile();
            sendMessage.setText("Yutqazding😎. Yana o'ynash uchun /go ni yubor");

        } else {

            if (wordDao.isUsed(text)) {
                sendMessage.setText(text + " so'z foydalanib bo'lingan. Boshqa so'z yubor");
            } else {
                wordDao.addUserWord(text);
                String word = wordDao.findWord(text.charAt(text.length() - 1));
                if (word != null) {
                    wordDao.addUserWord(word);
                    sendMessage.setText(word);
                } else {
                    sendMessage.setText("Yutqazdim😔. Yana o'ynash uchun /go ni yubor");
                    wordDao.refreshFile();
                }
            }
        }

        bot.sendMessage(sendMessage);
    }


}
