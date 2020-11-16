package com.tomi.payments.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tomi.payments.kafka.producer.KafkaProducerConfig;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ApiConfig extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("kafka-producer")
    private KafkaProducerConfig kafkaProducerConfig;

    @JsonProperty("kafka-producer")
    public KafkaProducerConfig getKafkaProducerConfig() {
        return kafkaProducerConfig;
    }

    @JsonProperty("kafka-producer")
    public void setKafkaProducerConfig(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaProducerConfig = kafkaProducerConfig;
    }
}
