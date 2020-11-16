package com.tomi.payments.domain;

import java.util.Objects;

public class CardTransaction {
    private String transactionId;
    private String cardNumber;
    private String cardExpiryDate;
    private String cardCvv;

    public CardTransaction() {
    }

    public CardTransaction(String transactionId, String cardNumber, String cardExpiryDate, String cardCvv) {
        this.transactionId = transactionId;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.cardCvv = cardCvv;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardTransaction that = (CardTransaction) o;
        return transactionId.equals(that.transactionId) &&
                cardNumber.equals(that.cardNumber) &&
                cardExpiryDate.equals(that.cardExpiryDate) &&
                cardCvv.equals(that.cardCvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, cardNumber, cardExpiryDate, cardCvv);
    }
}
