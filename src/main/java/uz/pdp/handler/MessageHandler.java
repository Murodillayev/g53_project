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


        if (text.equalsIgnoreCase("/start")) {
            wordDao.loadWords();
            sendMessage.setText("O'yinni boshlash uchun /start ni yubor");
        } else if (text.equalsIgnoreCase("/end")) {
            wordDao.refresh(chatId);
            sendMessage.setText("Yutqazding");

        } else {

            if (!wordDao.notUsed(text, chatId)) {
                sendMessage.setText(text + " so'z foydalanib bo'lingan. Boshqa so'z yubor");

            } else if (!validWord(text, chatId)) {
                String sign = wordDao.getLastWordEndSign(chatId);
                sendMessage.setText("<b>" + sign + "</b> harfiga so'z yubor");
            } else {
                wordDao.putUsed(text, chatId);
                String word = wordDao.findWord(text.charAt(text.length() - 1) + "", chatId);
                if (word != null) {
                    wordDao.putUsed(word, chatId);
                    sendMessage.setText(word);
                    wordDao.putLastWord(word, chatId);
                } else {
                    sendMessage.setText("Yutqazdim😔.");
                    wordDao.refresh(chatId);
                }
            }

        }

        bot.sendMessage(sendMessage);
    }

    private boolean validWord(String text, String chatId) {
        String sign = wordDao.getLastWordEndSign(chatId);

        return sign == null || text.startsWith(wordDao.getLastWordEndSign(chatId));
    }
}
