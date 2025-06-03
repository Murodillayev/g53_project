package uz.pdp.dao;

import com.google.gson.Gson;
import uz.pdp.model.Member;
import uz.pdp.model.Todo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class MemberDao {
    private static MemberDao instance;

    private final Gson gson = new Gson();
    private final String root = "/Users/macbookpro/Documents/pdp/word_game/src/main/resources/members";

    public static MemberDao getInstance() {
        if (instance == null) {
            instance = new MemberDao();
        }
        return instance;
    }

    public Optional<Member> findByChatId(String chatId) {

        Path path = Paths.get(root);
        try (Stream<Path> list = Files.list(path)) {

            Optional<Path> memberFile = list
                    .filter(f -> f.getFileName().toString().equals(chatId + ".json"))
                    .findFirst();

            if (memberFile.isPresent()) {
                String jsonData = String.join("", Files.readAllLines(memberFile.get()));

                Member member = gson.fromJson(jsonData, Member.class);

                return Optional.of(member);
            }
            return Optional.empty();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void addTodo(Todo tempTodo, String chatId) {
        Member member = findByChatId(chatId).orElseThrow(
                () -> new RuntimeException("Member not found")
        );
        tempTodo.setCompleted(false);
        tempTodo.setDeleted(false);
        tempTodo.setCreatedAt(LocalDateTime.now().toString());
        List<Todo> todos = member.getTodos();
        todos.add(tempTodo);
        member.setTodos(todos);
        save(member);

    }

    public void save(Member member) {
        Path path = Paths.get(root, member.getChatId() + ".json");
        String jsonData = gson.toJson(member, Member.class);
        try {
            if (Files.exists(path)) {
                Files.write(path, jsonData.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                create(member);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void create(Member member) {
        Path path = Paths.get(root, member.getChatId() + ".json");
        member.setId(UUID.randomUUID().toString());
        member.setCreatedAt(LocalDateTime.now().toString());
        member.setUpdatedAt(LocalDateTime.now().toString());
        member.setDeleted(false);
        String jsonData = gson.toJson(member, Member.class);

        try {
            Files.createFile(path);
            Files.write(path, jsonData.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
