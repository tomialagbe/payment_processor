package com.tomi.payments.representation;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;

public class CardAuthResponseTest {

    @Test
    public void canConvertToJSON() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String expected = objectMapper.writeValueAsString(
                objectMapper.readValue(fixture("card_auth_response.json"), CardAuthResponse.class));
        final CardAuthResponse cardAuthResponse = new CardAuthResponse(true, "3yQbSS4SAGz9ti6ONvLV9N");
        Assertions.assertEquals(objectMapper.writeValueAsString(cardAuthResponse), expected);
    }

    @Test
    void canConvertFromJSON() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final CardAuthResponse readResponse = objectMapper.readValue(fixture("card_auth_response.json"), CardAuthResponse.class);
        final CardAuthResponse cardAuthResponse = new CardAuthResponse(true, "3yQbSS4SAGz9ti6ONvLV9N");
        Assertions.assertEquals(readResponse.getSuccess(), cardAuthResponse.getSuccess());
        Assertions.assertEquals(readResponse.getTransactionId(), cardAuthResponse.getTransactionId());
    }
}
