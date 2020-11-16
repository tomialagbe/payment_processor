package com.tomi.payments.kafka.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KafkaJsonDeserializer<Payload> implements Deserializer<Payload> {
    private final Logger logger = LoggerFactory.getLogger(KafkaJsonDeserializer.class);
    private Class<Payload> payloadClass;
    public static final String CONFIG_KEY_CLASS = "value.deserializer.class";

    public KafkaJsonDeserializer() {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String className = String.valueOf(configs.get(CONFIG_KEY_CLASS));
        try {
            payloadClass = (Class<Payload>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("KafkaJsonDeserializer: Failed to configure deserializer - " + e.getMessage());
        }
    }

    @Override
    public Payload deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        Payload payload = null;
        try {
            payload = mapper.readValue(data, payloadClass);
        } catch (Exception e) {
            logger.error("KafkaJsonDeserializer: Failed to deserialize - " + e.getMessage());
        }
        return payload;
    }
}
