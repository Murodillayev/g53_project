package uz.pdp.handler;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import uz.pdp.*;
import uz.pdp.dao.MemberDao;
import uz.pdp.model.enums.Currency;
import uz.pdp.model.entity.Member;
import uz.pdp.model.entity.Setting;
import uz.pdp.utils.ButtonMaker;
import uz.pdp.utils.ButtonText;
import uz.pdp.utils.CallbackDataPrefix;

public class CallBackHandler {

    private final MemberDao memberDao = new MemberDao();
    private static CallBackHandler instance;
    private final ButtonMaker buttonMaker = ButtonMaker.getInstance();

    public static CallBackHandler getInstance() {
        if (instance == null) {
            instance = new CallBackHandler();
        }
        return instance;
    }

    public void handle(CallbackQuery callbackQuery) {
        CurrencyBot bot = new CurrencyBot();
        String data = callbackQuery.getData();
        int messId = callbackQuery.getMessage().getMessageId();
        String chatId = callbackQuery.getMessage().getChatId().toString();
        Member sessionMember = memberDao.getOrCreate(chatId);

        if (data.startsWith(CallbackDataPrefix.FROM_)) {
            String cur = data.replace(CallbackDataPrefix.FROM_, "").toUpperCase();
            setCurrency(sessionMember, cur, false);


            bot.editMessage(makeSettingEditMessage(messId, sessionMember));
//            bot.deleteMessage(makeDeleteMessage(messId, chatId));

        } else if (data.startsWith(CallbackDataPrefix.TO_)) {
            String cur = data.replace(CallbackDataPrefix.TO_, "").toUpperCase();
            setCurrency(sessionMember, cur, true);
//            bot.deleteMessage(makeDeleteMessage(messId, chatId));
            bot.editMessage(makeSettingEditMessage(messId, sessionMember));
        }
    }

    private EditMessageText makeSettingEditMessage(int messId, Member sessionMember) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(sessionMember.getChatId());
        editMessageText.setMessageId(messId);
        editMessageText.setText(ButtonText.SETTINGS);
        editMessageText.setReplyMarkup(buttonMaker.settingsButton(sessionMember));
        return editMessageText;
    }

    private void setCurrency(Member sessionMember, String cur, boolean to) {
        Currency currency = Currency.valueOf(cur);
        Setting setting = sessionMember.getSetting();
        if (to) {
            setting.setTo(currency);
        } else {
            setting.setFrom(currency);
        }
        sessionMember.setSetting(setting);
        memberDao.update(sessionMember);
    }


    private DeleteMessage makeDeleteMessage(int messageId, String chatId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(messageId);
        deleteMessage.setChatId(chatId);
        return deleteMessage;
    }
}
