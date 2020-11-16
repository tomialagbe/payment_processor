package com.tomi.payments.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tomi.payments.kafka.consumer.KafkaConsumerConfig;
import com.tomi.payments.kafka.producer.KafkaProducerConfig;
import io.dropwizard.Configuration;
import io.dropwizard.redis.RedisClientFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TokenizerConfig extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("kafka-producer")
    private KafkaProducerConfig kafkaProducerConfig;

    @Valid
    @NotNull
    @JsonProperty("kafka-consumer")
    private KafkaConsumerConfig kafkaConsumerConfig;

    @Valid
    @NotNull
    @JsonProperty("redis-client")
    private RedisClientFactory<String, String> redisClientFactory;

    @JsonProperty("kafka-producer")
    public KafkaProducerConfig getKafkaProducerConfig() {
        return kafkaProducerConfig;
    }

    @JsonProperty("kafka-producer")
    public void setKafkaProducerConfig(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaProducerConfig = kafkaProducerConfig;
    }

    @JsonProperty("kafka-consumer")
    public KafkaConsumerConfig getKafkaConsumerConfig() {
        return kafkaConsumerConfig;
    }

    @JsonProperty("kafka-consumer")
    public void setKafkaConsumerConfig(KafkaConsumerConfig kafkaConsumerConfig) {
        this.kafkaConsumerConfig = kafkaConsumerConfig;
    }

    @JsonProperty("redis-client")
    public RedisClientFactory<String, String> getRedisClientFactory() {
        return redisClientFactory;
    }

    @JsonProperty("redis-client")
    public void setRedisClientFactory(RedisClientFactory<String, String> redisClientFactory) {
        this.redisClientFactory = redisClientFactory;
    }
}
