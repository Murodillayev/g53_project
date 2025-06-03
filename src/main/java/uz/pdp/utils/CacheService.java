package uz.pdp.utils;

import uz.pdp.model.Todo;
import uz.pdp.model.enums.MemberState;

import java.util.HashMap;
import java.util.Map;

public class CacheService {
    private static CacheService instance;
    public static CacheService getInstance() {
        if (instance == null) {
            instance = new CacheService();
        }
        return instance;
    }

    private final Map<String, MemberState> STATES = new HashMap<>();
    private final Map<String, Todo> TEMP_TODOS = new HashMap<>();

    public void putState(String chatId, MemberState state) {
        STATES.put(chatId, state);
    }

    public MemberState getState(String chatId) {
        MemberState memberState = STATES.get(chatId);
        if (memberState == null) {
            return MemberState.UNKNOWN;
        }
        return memberState;
    }
    public void deleteState(String chatId) {
        STATES.remove(chatId);
    }


    public void putTodo(String chatId, Todo todo) {
        TEMP_TODOS.put(chatId, todo);
    }

    public Todo getTodo(String chatId) {
        return TEMP_TODOS.get(chatId);
    }


}
