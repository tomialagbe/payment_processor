package com.tomi.payments.kafka.producer;

import com.tomi.payments.kafka.serialization.KafkaJsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class KafkaProducerFactory {

    public KafkaProducerFactory() {
    }

    public <Payload> KafkaProducerClient<Payload> create(KafkaProducerConfig kafkaProducerConfig) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfig.getBootstrapServer());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfig.getAcks().orElse(""));
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfig.getRetries().orElse(0));
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaProducerConfig.getMaxInFlightRequestsPerConnection().orElse(1));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());
        return new KafkaProducerClient<>(kafkaProducerConfig.getTopic(), new KafkaProducer<>(props));
    }
}
