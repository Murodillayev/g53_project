package uz.pdp.handler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.dao.MemberDao;
import uz.pdp.model.Member;
import uz.pdp.service.TelegramService;
import uz.pdp.utils.CallBackPrefix;

public class CallBackHandler {

    private static CallBackHandler instance;
    private final TelegramService telegramService = TelegramService.getInstance();
    private final MemberDao memberDao = MemberDao.getInstance();

    public static CallBackHandler getInstance() {

        if (instance == null) {
            instance = new CallBackHandler();
        }


        return instance;
    }

    public void handle(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String data = callbackQuery.getData();
        int messageId = callbackQuery.getMessage().getMessageId();

        Member session = telegramService.registerMemberAndGet(callbackQuery.getFrom(), chatId);
        if (data.startsWith(CallBackPrefix.DONE)) {
            telegramService.doneTodo(data, session, callbackQuery);
        } else if (data.startsWith(CallBackPrefix.DELETE)) {
            telegramService.deleteTodo(data, session, messageId);
        }
    }

}