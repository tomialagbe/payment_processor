package com.tomi.payments.kafka.producer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerClient<Payload> {
    private final String topic;
    private final Producer<String, Payload> producer;

    public KafkaProducerClient(String topic, Producer<String, Payload> producer) {
        this.topic = topic;
        this.producer = producer;
    }

    public void produce(Payload payload) {
        producer.send(new ProducerRecord<>(topic, "payload", payload));
    }
}
