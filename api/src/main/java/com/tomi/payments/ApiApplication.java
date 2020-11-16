package com.tomi.payments;

import com.tomi.payments.config.ApiConfig;
import com.tomi.payments.domain.CardTransaction;
import com.tomi.payments.generator.TransactionIDGenerator;
import com.tomi.payments.kafka.producer.KafkaProducerClient;
import com.tomi.payments.kafka.producer.KafkaProducerFactory;
import com.tomi.payments.resource.AuthResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ApiApplication extends Application<ApiConfig> {

    @Override
    public void initialize(Bootstrap<ApiConfig> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(ApiConfig configuration, Environment environment) throws Exception {
        final TransactionIDGenerator transactionIDGenerator = new TransactionIDGenerator();
        KafkaProducerClient<CardTransaction> kafkaProducerClient = new KafkaProducerFactory().create(configuration.getKafkaProducerConfig());
        environment.jersey().register(new AuthResource(transactionIDGenerator, kafkaProducerClient));
    }

    public static void main(String[] args) throws Exception {
        new ApiApplication().run(args);
    }
}
