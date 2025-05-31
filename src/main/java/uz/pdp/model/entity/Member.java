package uz.pdp.model.entity;

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



