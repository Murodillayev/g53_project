package uz.pdp.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.pdp.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class ButtonMaker {
    private static ButtonMaker instance;

    public static ButtonMaker getInstance() {
        if (instance == null) {
            instance = new ButtonMaker();
        }
        return instance;
    }

    public ReplyKeyboard mainMenu() {
        ReplyKeyboardMarkup reply = new ReplyKeyboardMarkup();
        KeyboardButton add = new KeyboardButton();
        KeyboardButton todos = new KeyboardButton();
        add.setText(ButtonText.ADD);
        todos.setText(ButtonText.TASKS);

        KeyboardRow row = new KeyboardRow();
        row.add(add);
        row.add(todos);
        List<KeyboardRow> rows = new ArrayList<>(List.of(row));
        reply.setKeyboard(rows);
        reply.setSelective(true);
        reply.setResizeKeyboard(true);
        return reply;
    }

    public InlineKeyboardMarkup todoButtons(Todo todo) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        if (!todo.isCompleted()){
            InlineKeyboardButton doneButton = new InlineKeyboardButton();
            doneButton.setText(ButtonText.DONE);
            doneButton.setCallbackData(CallBackPrefix.DONE + todo.getId());
            row.add(doneButton);
        }

        InlineKeyboardButton deleteButton = new InlineKeyboardButton();
        deleteButton.setText(ButtonText.DELETE);
        deleteButton.setCallbackData(CallBackPrefix.DELETE + todo.getId());
        row.add(deleteButton);
        rows.add(row);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;

    }
}
