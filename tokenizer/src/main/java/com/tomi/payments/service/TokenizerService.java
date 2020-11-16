package com.tomi.payments.service;

import com.tomi.payments.crypto.CryptoService;
import com.tomi.payments.domain.CardTransaction;
import com.tomi.payments.domain.Token;
import com.tomi.payments.kafka.consumer.KafkaListener;
import com.tomi.payments.kafka.producer.KafkaProducerClient;
import com.tomi.payments.redis.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenizerService implements KafkaListener.Listener<CardTransaction> {
    private final Logger logger = LoggerFactory.getLogger(TokenizerService.class);

    private final CryptoService cryptoService;
    private final RedisClient<String, String> redisClient;
    private final KafkaProducerClient<Token> kafkaProducerClient;

    public TokenizerService(CryptoService cryptoService, RedisClient<String, String> redisClient, KafkaProducerClient<Token> kafkaProducerClient) {
        this.cryptoService = cryptoService;
        this.redisClient = redisClient;
        this.kafkaProducerClient = kafkaProducerClient;
    }

    @Override
    public void payloadReceived(CardTransaction cardTransaction) {
        try {
            logger.info("Received payload from api -> " + cardTransaction.getTransactionId());
            encryptTransactionAndSaveKey(cardTransaction);
        } catch (Exception e) {
            logger.error("Failed to handle card transaction", e);
        }
    }

    private void encryptTransactionAndSaveKey(CardTransaction transaction) throws Exception {
        final String key = cryptoService.generateKey(16);
        final String cipherText = cryptoService.AESEncrypt(key, transaction);
        logger.info("Encrypted transaction");
        redisClient.put(transaction.getTransactionId(), key);
        logger.info("Saved transaction encryption key");
        kafkaProducerClient.produce(new Token(transaction.getTransactionId(), cipherText));
        logger.info("Sent transaction cipher text to output");
    }
}
