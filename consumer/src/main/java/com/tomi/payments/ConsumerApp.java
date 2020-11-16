package com.tomi.payments;

import com.tomi.payments.config.ConsumerConfig;
import com.tomi.payments.crypto.CryptoService;
import com.tomi.payments.domain.Token;
import com.tomi.payments.kafka.consumer.KafkaListener;
import com.tomi.payments.redis.RedisClient;
import com.tomi.payments.service.ProofService;
import io.dropwizard.Application;
import io.dropwizard.redis.RedisClientBundle;
import io.dropwizard.redis.RedisClientFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.lettuce.core.api.StatefulRedisConnection;

public class ConsumerApp extends Application<ConsumerConfig> {

    private final RedisClientBundle<String, String, ConsumerConfig> redisBundle = new RedisClientBundle<String, String, ConsumerConfig>() {
        @Override
        public RedisClientFactory<String, String> getRedisClientFactory(ConsumerConfig configuration) {
            return configuration.getRedisClientFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<ConsumerConfig> bootstrap) {
        bootstrap.addBundle(redisBundle);
        super.initialize(bootstrap);
    }

    @Override
    public void run(ConsumerConfig configuration, Environment environment) throws Exception {
        final CryptoService cryptoService = new CryptoService();
        final StatefulRedisConnection<String, String> redisConnection = redisBundle.getConnection();
        final RedisClient<String, String> redisClient = new RedisClient<>(redisConnection);

        final ProofService proofService = new ProofService(cryptoService, redisClient);

        final KafkaListener<Token> kafkaListener =
                new KafkaListener<>(environment, configuration.getKafkaConsumerConfig(), proofService);
        kafkaListener.listen();
    }

    public static void main(String[] args) throws Exception {
        new ConsumerApp().run(args);
    }
}
