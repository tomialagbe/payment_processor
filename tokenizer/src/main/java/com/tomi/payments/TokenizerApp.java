package com.tomi.payments;

import com.tomi.payments.config.TokenizerConfig;
import com.tomi.payments.crypto.CryptoService;
import com.tomi.payments.domain.CardTransaction;
import com.tomi.payments.domain.Token;
import com.tomi.payments.kafka.consumer.KafkaListener;
import com.tomi.payments.kafka.producer.KafkaProducerClient;
import com.tomi.payments.kafka.producer.KafkaProducerFactory;
import com.tomi.payments.redis.RedisClient;
import com.tomi.payments.service.TokenizerService;
import io.dropwizard.Application;
import io.dropwizard.redis.RedisClientBundle;
import io.dropwizard.redis.RedisClientFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.lettuce.core.api.StatefulRedisConnection;

public class TokenizerApp extends Application<TokenizerConfig> {

    private final RedisClientBundle<String, String, TokenizerConfig> redisBundle = new RedisClientBundle<String, String, TokenizerConfig>() {
        @Override
        public RedisClientFactory<String, String> getRedisClientFactory(TokenizerConfig configuration) {
            return configuration.getRedisClientFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<TokenizerConfig> bootstrap) {
        bootstrap.addBundle(redisBundle);
        super.initialize(bootstrap);
    }

    @Override
    public void run(TokenizerConfig configuration, Environment environment) throws Exception {
        final CryptoService cryptoService = new CryptoService();
        final StatefulRedisConnection<String, String> redisConnection = redisBundle.getConnection();
        final RedisClient<String, String> redisClient = new RedisClient<>(redisConnection);
        final KafkaProducerClient<Token> kafkaProducerClient = new KafkaProducerFactory().create(configuration.getKafkaProducerConfig());

        final TokenizerService tokenizerService = new TokenizerService(cryptoService, redisClient, kafkaProducerClient);

        final KafkaListener<CardTransaction> kafkaListener =
                new KafkaListener<>(environment, configuration.getKafkaConsumerConfig(), tokenizerService);
        kafkaListener.listen();
    }

    public static void main(String[] args) throws Exception {
        new TokenizerApp().run(args);
    }

}
