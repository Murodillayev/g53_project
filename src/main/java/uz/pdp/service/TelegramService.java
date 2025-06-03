package uz.pdp.service;

import com.google.gson.Gson;
import org.apache.commons.lang3.text.StrBuilder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.pdp.CurrencyBot;
import uz.pdp.model.entity.CurrencyModel;
import uz.pdp.model.entity.Member;
import uz.pdp.model.entity.Setting;
import uz.pdp.model.enums.Currency;
import uz.pdp.utils.ButtonMaker;
import uz.pdp.utils.ButtonText;
import uz.pdp.utils.ConstMessage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class TelegramService {

    private static TelegramService instance;
    private final ButtonMaker buttonMaker = ButtonMaker.getInstance();
    private CurrencyBot bot;

    private TelegramService() {
    }

    public static TelegramService getInstance() {
        if (instance == null) {
            instance = new TelegramService();
        }
        return instance;
    }


    public void sendWelcome(SendMessage sendMessage, Member member) {
        bot = new CurrencyBot();
        sendMessage.setText(ConstMessage.WELCOME_MESSAGE.formatted(member.getName()));
        sendMessage.setReplyMarkup(buttonMaker.mainMenuButtons());
        bot.sendMessage(sendMessage);
    }

    public void sendSettings(SendMessage sendMessage, Member sessionMember) {
        bot = new CurrencyBot();
        sendMessage.setText(ButtonText.SETTINGS);
        sendMessage.setReplyMarkup(buttonMaker.settingsButton(sessionMember));
        bot.sendMessage(sendMessage);
    }

    public void sendResult(String fromValue, SendMessage sendMessage, Member sessionMember) {
        bot = new CurrencyBot();
        Setting setting = sessionMember.getSetting();
        double result;

        if (setting.getFrom().equals(setting.getTo())) {
            result = Double.parseDouble(fromValue);

        } else if (!setting.getFrom().equals(Currency.UZS) && !setting.getTo().equals(Currency.UZS)) {
            Double fromUzsValue = toUZS(Double.parseDouble(fromValue), setting.getFrom());
            Double toUzsValue = toUZS(Double.parseDouble(fromValue), setting.getTo());
            result = fromUzsValue / toUzsValue;

        } else if (setting.getFrom().equals(Currency.UZS)) {
            Double toUzsValue = toUZS(1., setting.getTo());
            result = Double.parseDouble(fromValue) / toUzsValue;

        } else {
            result = toUZS(Double.parseDouble(fromValue), setting.getFrom());
        }

        String resultText = ConstMessage.RESULT_TEXT.formatted(fromValue, setting.getFrom(), result, setting.getTo());
        sendMessage.setText(resultText);
        bot.sendMessage(sendMessage);
    }

    private Double toUZS(Double value, Currency currency) {

        CurrencyModel currencyData = getCurrency(currency);
        assert currencyData != null;

        return Double.parseDouble(currencyData.getRate()) * value;
    }

    private CurrencyModel getCurrency(Currency currency) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            String url = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/%s/%s/".formatted(currency.name(), LocalDate.now());
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            CurrencyModel[] currencyModels = new Gson().fromJson(response.body(), CurrencyModel[].class);
            return currencyModels.length == 0 ? null : currencyModels[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void postChannel() {
        CurrencyModel usd = getCurrency(Currency.USD);
        String text = prepareChannelPostText(usd);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId("-1001317389925"); // channel username
        new CurrencyBot().sendMessage(sendMessage);
    }

    private String prepareChannelPostText(CurrencyModel usd) {
        return "===== " + usd.getDate() + " =====\n" +
                "\uD83D\uDCB0 Bozor kursi\n\n" +
                "Sotish: " + usd.getRate() + " so‘m\n" +
                "Olish: " + usd.getRate() + " so‘m\n";
    }
}


// usd => uzs
// usd_uzs   uzs

// usd => rub
// uzs => usd