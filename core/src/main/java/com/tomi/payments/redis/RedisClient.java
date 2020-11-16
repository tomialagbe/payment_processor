package com.tomi.payments.redis;

import io.lettuce.core.api.StatefulRedisConnection;

public class RedisClient<Key, Value> {
    private final StatefulRedisConnection<Key, Value> redisConnection;

    public RedisClient(StatefulRedisConnection<Key, Value> redisConnection) {
        this.redisConnection = redisConnection;
    }

    public void put(Key key, Value value) {
        redisConnection.sync().set(key, value);
    }

    public Value get(Key key) {
        return redisConnection.sync().get(key);
    }
}
