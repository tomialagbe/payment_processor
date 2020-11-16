package com.tomi.payments.kafka;

import com.tomi.payments.kafka.producer.KafkaProducerClient;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class KafkaProducerClientTest {

    @Test
    public void produceCallsKafkaProducer() {
        Producer<String, Integer> mockProducer = mock(Producer.class);//new MockProducer<>(true, new StringSerializer(), new IntegerSerializer());
        KafkaProducerClient<Integer> kafkaProducerClient = new KafkaProducerClient<>("test-topic", mockProducer);
        kafkaProducerClient.produce(10);
        verify(mockProducer).send(any());
    }

    @Test
    public void produceUpdatesProducerHistory() {
        MockProducer<String, Integer> mockProducer = new MockProducer<>(true, new StringSerializer(), new IntegerSerializer());
        KafkaProducerClient<Integer> kafkaProducerClient = new KafkaProducerClient<>("test-topic", mockProducer);
        kafkaProducerClient.produce(10);
        kafkaProducerClient.produce(20);
        Assertions.assertEquals(mockProducer.history().size(), 2);
    }
}
