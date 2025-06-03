package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Todo extends BaseModel {
    private String title;
    private String description;
    private boolean completed;
    private String remindTime;



}
