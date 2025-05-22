package uz.pdp.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordDao {
    private static WordDao instance;

    private final String root = "src/main/resources/words.txt";

    private List<String> WORDS = new ArrayList<>();

    private final List<String> USED_WORDS = new ArrayList<>();

    private WordDao() {

    }

    public static WordDao getInstance() {
        if (instance == null) {
            instance = new WordDao();
        }
        return instance;
    }


    public String findWord(char start) {
        for (int i = 0; i < WORDS.size(); i++) {
            String word = WORDS.get(i);
            if (word.toLowerCase().startsWith(String.valueOf(start).toLowerCase()) && !isUsed(word)) {
                WORDS.remove(i);
                return word;
            }
        }
        return null;
    }

    public void refreshFile() {
        Set<String> wordsSet = new HashSet<>();
        wordsSet.addAll(WORDS);
        wordsSet.addAll(USED_WORDS);
        saveWords(wordsSet);

    }

    private void saveWords(Set<String> wordsSet) {
        StringBuilder words = new StringBuilder();
        for (String s : wordsSet) {
            words.append(s).append(System.lineSeparator());
        }
        try {
            Files.write(Paths.get(root), words.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Can't save words");
        }
    }

    public void resetWords() {
        try (Stream<String> stream = Files.lines(Paths.get(root))) {
            WORDS = stream.collect(Collectors.toList());
            USED_WORDS.clear();
        } catch (IOException e) {
            System.out.println("Can't load words");
        }
    }

    public void addUserWord(String text) {
        USED_WORDS.add(text);
    }

    public boolean isUsed(String text) {
        return USED_WORDS.contains(text);
    }
}
