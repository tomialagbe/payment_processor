package com.tomi.payments;

import com.tomi.payments.crypto.CryptoService;
import com.tomi.payments.domain.CardTransaction;
import com.tomi.payments.domain.Token;
import com.tomi.payments.kafka.producer.KafkaProducerClient;
import com.tomi.payments.redis.RedisClient;
import com.tomi.payments.service.TokenizerService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class TokenizerServiceTest {
    final ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.forClass(Token.class);

    @Test
    public void testPayloadReceived() {
        final CardTransaction cardTransaction = new CardTransaction("1sahks28AS412762121", "212312129984", "09/23", "129");

        final CryptoService cryptoService = mock(CryptoService.class);
        final RedisClient<String, String> redisClient = mock(RedisClient.class);
        final KafkaProducerClient<Token> kafkaProducerClient = mock(KafkaProducerClient.class);

        final String mockKey = "ashhgetyastbGASu";
        final String mockCipherText = "2C9DmKME4Zx9S54CG6TRGj2C9DmKME4Zx9S54CG6TRGj2C9DmKME4Zx9S54CG6TRGj";
        when(cryptoService.generateKey(16)).thenReturn(mockKey);
        when(cryptoService.AESEncrypt(mockKey, cardTransaction)).thenReturn(mockCipherText);

        TokenizerService tokenizerService = new TokenizerService(cryptoService, redisClient, kafkaProducerClient);
        tokenizerService.payloadReceived(cardTransaction);

        verify(cryptoService).generateKey(16);
        verify(cryptoService).AESEncrypt(mockKey, cardTransaction);
        verify(kafkaProducerClient).produce(tokenArgumentCaptor.capture());
        Assertions.assertEquals(tokenArgumentCaptor.getValue().getTransactionId(), cardTransaction.getTransactionId());
        Assertions.assertEquals(tokenArgumentCaptor.getValue().getValue(), mockCipherText);

    }

}
