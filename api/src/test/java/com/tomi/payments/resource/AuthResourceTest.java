package com.tomi.payments.resource;

import com.tomi.payments.domain.CardTransaction;
import com.tomi.payments.generator.TransactionIDGenerator;
import com.tomi.payments.kafka.producer.KafkaProducerClient;
import com.tomi.payments.representation.CardAuthResponse;
import com.tomi.payments.representation.CardDto;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class AuthResourceTest {
    private static final TransactionIDGenerator transactionIDGenerator = mock(TransactionIDGenerator.class);
    private static final KafkaProducerClient<CardTransaction> kafkaClient = mock(KafkaProducerClient.class);

    private ArgumentCaptor<CardTransaction> cardTransactionCaptor = ArgumentCaptor.forClass(CardTransaction.class);

    public static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new AuthResource(transactionIDGenerator, kafkaClient))
            .build();

    @Test
    void createsCardTransaction() {
        final CardDto cardDto = new CardDto("1234567890", "09/22", "129");

        when(transactionIDGenerator.generateID()).thenReturn("3yQbSS4SAGz9ti6ONvLV9N");
        doNothing().when(kafkaClient).produce(any());

        CardAuthResponse response = resources.target("/api/auth")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(cardDto, MediaType.APPLICATION_JSON), CardAuthResponse.class);

        verify(transactionIDGenerator).generateID();
        verify(kafkaClient).produce(cardTransactionCaptor.capture());

        Assertions.assertEquals(cardTransactionCaptor.getValue().getTransactionId(), "3yQbSS4SAGz9ti6ONvLV9N");
        Assertions.assertEquals(cardTransactionCaptor.getValue().getCardCvv(), cardDto.getCvv());
        Assertions.assertEquals(cardTransactionCaptor.getValue().getCardExpiryDate(), cardDto.getExpirationDate());

        Assertions.assertEquals(response.getTransactionId(), "3yQbSS4SAGz9ti6ONvLV9N");
        Assertions.assertEquals(response.getSuccess(), true);
    }

    @Test
    void failsToCreateCardTransactionWithInvalidCVV() {
        when(transactionIDGenerator.generateID()).thenReturn("3yQbSS4SAGz9ti6ONvLV9N");
        doNothing().when(kafkaClient).produce(any());

        final CardDto cardDto = new CardDto("1234567890", "09/22", "12913");
        Response response = resources.target("/api/auth")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(cardDto, MediaType.APPLICATION_JSON), Response.class);

        Assertions.assertEquals(response.getStatus(), 422);
    }

    @Test
    void failsToCreateCardTransactionWithInvalidExpiryDate() {
        final CardDto cardDto = new CardDto("1234567890", "092/22", "129");
        Response response = resources.target("/api/auth")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(cardDto, MediaType.APPLICATION_JSON), Response.class);

        Assertions.assertEquals(response.getStatus(), 422);
    }

    @Test
    void failsToCreateCardTransactionWithInvalidCardNumber() {
        final CardDto cardDto = new CardDto("1234", "02/22", "129");
        Response response = resources.target("/api/auth")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(cardDto, MediaType.APPLICATION_JSON), Response.class);

        Assertions.assertEquals(response.getStatus(), 422);
    }

}
