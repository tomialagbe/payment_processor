package com.tomi.payments.kafka.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KafkaJsonSerializer<T> implements Serializer<T> {

    private final Logger logger = LoggerFactory.getLogger(KafkaJsonSerializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, T data) {
        byte[] serialized = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            serialized = objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            logger.error("KafkaJsonSerializer: Failed to serialize - " + e.getMessage());
        }
        return serialized;
    }

    @Override
    public void close() {
    }
}
