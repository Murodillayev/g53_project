package uz.pdp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.pdp.model.enums.Currency;

@Getter
@Setter
@AllArgsConstructor
public class Setting {
    private Currency from;
    private Currency to;
}
