package uz.pdp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Member {
    private String id;
    private String name;
    private String phone;
    private String chatId;
    private Setting setting;


}



