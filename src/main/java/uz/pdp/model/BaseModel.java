package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BaseModel {
    private String id;
    private String createdAt;
    private String updatedAt;
    private Boolean deleted;

    public BaseModel() {
        id = UUID.randomUUID().toString();
    }
}
