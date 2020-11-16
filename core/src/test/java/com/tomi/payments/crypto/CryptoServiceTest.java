package com.tomi.payments.crypto;

import com.tomi.payments.domain.CardTransaction;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CryptoServiceTest {

    @Test
    public void generatesKeyOfSpecifiedLength() {
        final CryptoService cryptoService = new CryptoService();
        String key = cryptoService.generateKey(10);
        Assertions.assertEquals(key.length(), 10);

        key = cryptoService.generateKey(12);
        Assertions.assertEquals(key.length(), 12);
    }

    @Test
    public void encryptsAndDecrypts() {
        final CryptoService cryptoService = new CryptoService();
        final String key = "asui217746@AH!GT";
        //(String transactionId, String cardNumber, String cardExpiryDate, String cardCvv)
        final CardTransaction cardTransaction = new CardTransaction("1sahks28AS412762121", "212312129984", "09/23", "129");
        final String cipherText = cryptoService.AESEncrypt(key, cardTransaction);
        Assertions.assertNotNull(cipherText);

        final CardTransaction decryptedTransaction = cryptoService.AESDecrypt(key, cipherText, CardTransaction.class);
        Assertions.assertEquals(decryptedTransaction.getTransactionId(), cardTransaction.getTransactionId());
        Assertions.assertEquals(decryptedTransaction.getCardNumber(), cardTransaction.getCardNumber());
        Assertions.assertEquals(decryptedTransaction.getCardExpiryDate(), cardTransaction.getCardExpiryDate());
        Assertions.assertEquals(decryptedTransaction.getCardCvv(), cardTransaction.getCardCvv());
    }
}
