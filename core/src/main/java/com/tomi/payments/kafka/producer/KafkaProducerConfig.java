package com.tomi.payments.kafka.producer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.util.Duration;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public class KafkaProducerConfig implements Discoverable {

    @NotEmpty
    @JsonProperty
    private String topic;

    @NotEmpty
    @JsonProperty
    private String bootstrapServer;

    @JsonProperty
    private Optional<String> acks = Optional.empty();

    @JsonProperty
    private Optional<Integer> retries = Optional.empty();

    @JsonProperty
    private Optional<Integer> maxInFlightRequestsPerConnection = Optional.empty();

    @JsonProperty
    private Optional<Duration> maxPollBlockTime = Optional.empty();

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBootstrapServer() {
        return bootstrapServer;
    }

    public void setBootstrapServer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }

    public Optional<String> getAcks() {
        return acks;
    }

    public void setAcks(Optional<String> acks) {
        this.acks = acks;
    }

    public Optional<Integer> getRetries() {
        return retries;
    }

    public void setRetries(Optional<Integer> retries) {
        this.retries = retries;
    }

    public Optional<Integer> getMaxInFlightRequestsPerConnection() {
        return maxInFlightRequestsPerConnection;
    }

    public void setMaxInFlightRequestsPerConnection(Optional<Integer> maxInFlightRequestsPerConnection) {
        this.maxInFlightRequestsPerConnection = maxInFlightRequestsPerConnection;
    }

    public Optional<Duration> getMaxPollBlockTime() {
        return maxPollBlockTime;
    }

    public void setMaxPollBlockTime(Optional<Duration> maxPollBlockTime) {
        this.maxPollBlockTime = maxPollBlockTime;
    }
}