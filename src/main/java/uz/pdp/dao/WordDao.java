package uz.pdp.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

public class WordDao {
    private static WordDao instance;

    private final String root = "src/main/resources/words.txt";
    private final Set<String> WORDS = new HashSet<>();
    private final Map<String, String> LAST_WORDS = new HashMap<>();
    private final Map<String, List<String>> USER_WORDS_MAP = new HashMap<>();

    public static WordDao getInstance() {
        if (instance == null) {
            instance = new WordDao();
        }
        return instance;
    }


    public String findWord(String startsWith, String chatId) {
        Stream<String> wordsStream = WORDS.stream();
        Optional<String> first = wordsStream
                .filter(e -> e.startsWith(startsWith) && notUsed(e, chatId))
                .findFirst();

        return first.orElse(null);
    }

    public void putUsed(String word, String chatId) {
        List<String> words = USER_WORDS_MAP.get(chatId);
        if (words == null) {
            words = new ArrayList<>();
        }
        words.add(word);
        USER_WORDS_MAP.put(chatId, words);
    }

    public void refresh(String chatId) {
        List<String> usedWords = USER_WORDS_MAP.get(chatId);
        List<String> newWords = usedWords.stream()
                .filter(word -> !WORDS.contains(word))
                .toList();


        USER_WORDS_MAP.get(chatId).clear();
        WORDS.addAll(newWords);
        addNewWords(newWords);
    }

    private void addNewWords(List<String> newWords) {
        StringBuilder words = new StringBuilder();
        for (String s : newWords) {
            words.append(s).append(System.lineSeparator());
        }
        try {
            Files.write(Paths.get(root), words.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Can't save words");
        }

    }

    public boolean notUsed(String word, String chatId) {
        List<String> used = USER_WORDS_MAP.get(chatId);
        return used == null || !used.contains(word);
    }

    public void loadWords() {
        try {
            WORDS.addAll(Files.readAllLines(Paths.get(root)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getLastWordEndSign(String chatId) {
        String lastWord = LAST_WORDS.get(chatId);
        if (lastWord == null) {
            return null;
        }
        return lastWord.charAt(lastWord.length() - 1) + "";
    }

    public void putLastWord(String word, String chatId) {
        LAST_WORDS.put(chatId, word);
    }
}
