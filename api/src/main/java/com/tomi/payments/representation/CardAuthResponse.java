package com.tomi.payments.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardAuthResponse {

    private final boolean success;
    private final String transactionId;

    @JsonCreator
    public CardAuthResponse(@JsonProperty("success") boolean success, @JsonProperty("transactionId") String transactionId) {
        this.success = success;
        this.transactionId = transactionId;
    }

    @JsonProperty
    public boolean getSuccess() {
        return success;
    }

    @JsonProperty
    public String getTransactionId() {
        return transactionId;
    }
}
