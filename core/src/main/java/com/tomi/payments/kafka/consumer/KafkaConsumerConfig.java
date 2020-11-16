package com.tomi.payments.kafka.consumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.Discoverable;

import javax.validation.constraints.NotEmpty;

public class KafkaConsumerConfig implements Discoverable {

    @NotEmpty
    @JsonProperty
    protected String bootstrapServer;

    @NotEmpty
    @JsonProperty
    protected String topic;

    @NotEmpty
    @JsonProperty
    protected String deserializerClass;

    @NotEmpty
    @JsonProperty
    protected String groupId;

    public String getBootstrapServer() {
        return bootstrapServer;
    }

    public void setBootstrapServer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDeserializerClass() {
        return deserializerClass;
    }

    public void setDeserializerClass(String deserializerClass) {
        this.deserializerClass = deserializerClass;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
