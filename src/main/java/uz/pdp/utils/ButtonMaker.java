package uz.pdp.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.pdp.model.entity.Member;
import uz.pdp.model.entity.Setting;

import java.util.ArrayList;
import java.util.List;

public class ButtonMaker {
    private static ButtonMaker instance = new ButtonMaker();

    public static ButtonMaker getInstance() {
        if (instance == null) {
            instance = new ButtonMaker();
        }
        return instance;
    }


    public ReplyKeyboardMarkup mainMenuButtons() {
        ReplyKeyboardMarkup reply = new ReplyKeyboardMarkup();
        KeyboardButton button = new KeyboardButton();
        button.setText(ButtonText.SETTINGS);

        KeyboardRow row = new KeyboardRow();
        row.add(button);
        List<KeyboardRow> rows = new ArrayList<>(List.of(row));
        reply.setKeyboard(rows);

        return reply;

    }


    public InlineKeyboardMarkup settingsButton(Member member) {
        InlineKeyboardMarkup inline = new InlineKeyboardMarkup();

        InlineKeyboardButton fromUzs = new InlineKeyboardButton();
        InlineKeyboardButton toUzs = new InlineKeyboardButton();
        InlineKeyboardButton fromUsd = new InlineKeyboardButton();
        InlineKeyboardButton toUsd = new InlineKeyboardButton();

        Setting setting = member.getSetting();

        switch (setting.getFrom()) {
            case USD -> {
                fromUsd.setText(ButtonText.USD + " ✅");
                fromUzs.setText(ButtonText.UZS);
            }
            case UZS -> {
                fromUsd.setText(ButtonText.USD);
                fromUzs.setText(ButtonText.UZS + " ✅");
            }
        }

        switch (setting.getTo()) {
            case USD -> {
                toUzs.setText(ButtonText.UZS);
                toUsd.setText(ButtonText.USD + " ✅");

            }
            case UZS -> {
                toUzs.setText(ButtonText.UZS + " ✅");
                toUsd.setText(ButtonText.USD);

            }
        }

        toUzs.setCallbackData("to_uzs");
        toUsd.setCallbackData("to_usd");
        fromUzs.setCallbackData("from_uzs");
        fromUsd.setCallbackData("from_usd");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        row1.add(fromUzs);
        row1.add(toUzs);

        row2.add(fromUsd);
        row2.add(toUsd);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>(List.of(row1, row2));

        inline.setKeyboard(rows);

        return inline;

    }
}
