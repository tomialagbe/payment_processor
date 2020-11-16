package com.tomi.payments;

import com.tomi.payments.crypto.CryptoService;
import com.tomi.payments.domain.CardTransaction;
import com.tomi.payments.domain.Token;
import com.tomi.payments.redis.RedisClient;
import com.tomi.payments.service.ProofService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class ProofServiceTest {

    @Test
    public void testPayloadReceived() {
        final Token token = new Token("dmbacXUA4PwaaQuFODcgI", "dmbacXUA4PwaaQuFODcgIdmbacXUA4PwaaQuFODcgIdmbacXUA4PwaaQuFODcgIdmbacXUA4PwaaQuFODcgI");

        final CryptoService cryptoService = mock(CryptoService.class);
        final RedisClient<String, String> redisClient = mock(RedisClient.class);

        final String mockKey = "ashhgetyastbGASu";
        final CardTransaction cardTransaction = new CardTransaction("dmbacXUA4PwaaQuFODcgI", "212312129984", "09/23", "129");
        when(redisClient.get("dmbacXUA4PwaaQuFODcgI")).thenReturn(mockKey);
        when(cryptoService.AESDecrypt(mockKey, "dmbacXUA4PwaaQuFODcgIdmbacXUA4PwaaQuFODcgIdmbacXUA4PwaaQuFODcgIdmbacXUA4PwaaQuFODcgI", CardTransaction.class)).thenReturn(cardTransaction);

        final ProofService proofService = new ProofService(cryptoService, redisClient);
        proofService.payloadReceived(token);

        verify(cryptoService).AESDecrypt(mockKey, "dmbacXUA4PwaaQuFODcgIdmbacXUA4PwaaQuFODcgIdmbacXUA4PwaaQuFODcgIdmbacXUA4PwaaQuFODcgI", CardTransaction.class);
        verify(redisClient).get("dmbacXUA4PwaaQuFODcgI");
    }
}
