package uz.pdp.model.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyModel {
    private Long id;

    @SerializedName("Code")
    private String code;

    @SerializedName("Ccy")
    private String ccy;

    @SerializedName("CcyNm_RU")
    private String ccyNmRu;

    @SerializedName("CcyNm_UZ")
    private String ccyNmUz;

    @SerializedName("CcyNm_UZC")
    private String ccyNmUzc;

    @SerializedName("CcyNm_EN")
    private String ccyNmEn;

    @SerializedName("Nominal")
    private String nominal;

    @SerializedName("Rate")
    private String rate;

    @SerializedName("Diff")
    private String diff;

    @SerializedName("Date")
    private String date;
}
