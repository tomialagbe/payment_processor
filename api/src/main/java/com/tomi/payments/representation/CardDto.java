package com.tomi.payments.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CardDto {
    @NotBlank
    @Length(
            min = 10,
            max = 19
    )
    private final String cardNumber;

    @NotBlank
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})$", message = "Date must be in the format dd/yy")
    private final String expirationDate;

    @NotBlank
    @Length(min = 3, max = 3)
    private final String cvv;

    @JsonCreator
    public CardDto(@JsonProperty("cardNumber") String cardNumber, @JsonProperty("expirationDate") String expirationDate, @JsonProperty("cvv") String cvv) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }

    @JsonProperty
    public String getCardNumber() {
        return cardNumber;
    }

    @JsonProperty
    public String getExpirationDate() {
        return expirationDate;
    }

    @JsonProperty
    public String getCvv() {
        return cvv;
    }
}
