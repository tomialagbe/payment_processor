package com.tomi.payments.redis;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class RedisClientTest {


    @Test
    void testPut() {
        final StatefulRedisConnection<String, String> redisConnection = mock(StatefulRedisConnection.class);
        final RedisCommands<String, String> redisCommands = mock(RedisCommands.class);
        when(redisCommands.set(any(), any())).thenReturn("reply");

        when(redisConnection.sync()).thenReturn(redisCommands);
        final RedisClient<String, String> redisClient = new RedisClient<>(redisConnection);
        redisClient.put("key", "value");

         ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

        verify(redisCommands).set(keyCaptor.capture(), valueCaptor.capture());
        Assertions.assertEquals(keyCaptor.getValue(), "key");
        Assertions.assertEquals(valueCaptor.getValue(), "value");
    }
}
