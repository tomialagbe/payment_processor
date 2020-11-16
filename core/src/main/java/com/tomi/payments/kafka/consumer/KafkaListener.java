package com.tomi.payments.kafka.consumer;

import com.tomi.payments.kafka.serialization.KafkaJsonDeserializer;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaListener<Payload> implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(KafkaListener.class);

    private final Environment environment;
    private KafkaConsumer<String, Payload> kafkaConsumer;
    private Listener<Payload> payloadListener;

    public KafkaListener(Environment environment, KafkaConsumerConfig consumerConfig, Listener<Payload> payloadListener) {
        this.environment = environment;
        this.payloadListener = payloadListener;
        initializeConsumer(consumerConfig);
    }

    private void initializeConsumer(KafkaConsumerConfig consumerConfig) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfig.groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, (int) Duration.ofSeconds(300).toMillis());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonDeserializer.class.getName());
        props.put("value.deserializer.class", consumerConfig.deserializerClass);
        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Collections.singletonList(consumerConfig.topic));
    }

    public void listen() {
        environment.lifecycle().manage(new ManagedKafkaListener());
    }

    @Override
    public void run() {
        for (; ; ) {
            final Duration halfASecond = Duration.ofMillis(500);
            ConsumerRecords<String, Payload> payloadRecords = kafkaConsumer.poll(halfASecond);

            for (ConsumerRecord<String, Payload> record : payloadRecords) {
                logger.info("Received payload: ", record.key(), record.value());
                payloadListener.payloadReceived(record.value());
            }
        }
    }

    public interface Listener<Payload> {
        void payloadReceived(Payload payload);
    }

    private class ManagedKafkaListener implements Managed {
        private final ExecutorService service = Executors.newSingleThreadExecutor();

        @Override
        public void start() throws Exception {
            service.execute(KafkaListener.this);
        }

        @Override
        public void stop() throws Exception {
            service.shutdown();
        }
    }
}
