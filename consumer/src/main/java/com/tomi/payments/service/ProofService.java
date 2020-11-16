package com.tomi.payments.service;

import com.tomi.payments.crypto.CryptoService;
import com.tomi.payments.domain.CardTransaction;
import com.tomi.payments.domain.Token;
import com.tomi.payments.kafka.consumer.KafkaListener;
import com.tomi.payments.redis.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProofService implements KafkaListener.Listener<Token> {
    private final Logger logger = LoggerFactory.getLogger(ProofService.class);

    private final CryptoService cryptoService;
    private final RedisClient<String, String> redisClient;

    public ProofService(CryptoService cryptoService, RedisClient<String, String> redisClient) {
        this.cryptoService = cryptoService;
        this.redisClient = redisClient;
    }

    @Override
    public void payloadReceived(Token token) {
        try {
            logger.info("Received payload from tokenizer -> " + token.getTransactionId());
            decryptAndPrintToken(token);
        } catch (Exception ex) {
            logger.error("Failed to handle token", ex);
        }
    }

    private void decryptAndPrintToken(Token token) {
        String key = redisClient.get(token.getTransactionId());
        String cipherText = token.getValue();
        CardTransaction cardTransaction = cryptoService.AESDecrypt(key, cipherText, CardTransaction.class);
        logger.info("====================================");
        logger.info("Retrieved card transaction");
        logger.info("Txn ID: " + cardTransaction.getTransactionId());
        logger.info("Card number: " + cardTransaction.getCardNumber());
        logger.info("Card cvv: " + cardTransaction.getCardCvv());
        logger.info("Card expiry: " + cardTransaction.getCardExpiryDate());
        logger.info("====================================");
    }
}
