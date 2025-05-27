package uz.pdp;

import java.util.*;


public class MemberDao {
    private static final List<Member> members = new ArrayList<>();

    public Member getOrCreate(String chatId) {
        Optional<Member> first = members.stream().filter(
                member -> member.getChatId().equals(chatId)
        ).findFirst();

        if (first.isPresent()) {
            return first.get();
        }
        Member newMember = Member.builder()
                .chatId(chatId)
                .id(UUID.randomUUID().toString().replace("-", ""))
                .setting(new Setting(Currency.USD, Currency.UZS))
                .build();
        members.add(newMember);
        return newMember;
    }

    public void update(Member member) {


        members.stream()
                .filter(
                        m -> m.getId().equals(member.getId())
                ).findFirst()
                .ifPresent(
                        (m) -> {
                            m.setChatId(member.getChatId());
                            m.setSetting(member.getSetting());
                            m.setPhone(member.getPhone());
                            m.setName(member.getName());
                        }
                );

    }

}
