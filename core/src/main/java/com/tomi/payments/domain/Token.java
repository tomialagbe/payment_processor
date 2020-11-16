package com.tomi.payments.domain;

public class Token {

    private String transactionId;
    private String value;

    public Token() {
    }

    public Token(String transactionId, String value) {
        this.transactionId = transactionId;
        this.value = value;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
