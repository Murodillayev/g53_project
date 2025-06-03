package uz.pdp;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.pdp.service.TelegramService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotInitializer {
    public static void main(String[] args) {
        try {
            CurrencyBot bot = new CurrencyBot();
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(bot);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            TelegramService.getInstance().postChannel();
        }, 1, 10, TimeUnit.SECONDS);

    }
}