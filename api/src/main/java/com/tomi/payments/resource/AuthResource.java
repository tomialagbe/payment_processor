package com.tomi.payments.resource;

import com.tomi.payments.domain.CardTransaction;
import com.tomi.payments.generator.TransactionIDGenerator;
import com.tomi.payments.kafka.producer.KafkaProducerClient;
import com.tomi.payments.representation.CardAuthResponse;
import com.tomi.payments.representation.CardDto;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final TransactionIDGenerator idGenerator;
    private final KafkaProducerClient<CardTransaction> kafkaProducerClient;

    public AuthResource(TransactionIDGenerator idGenerator, KafkaProducerClient<CardTransaction> kafkaProducerClient) {
        this.idGenerator = idGenerator;
        this.kafkaProducerClient = kafkaProducerClient;
    }

    @POST
    @Path("/auth")
    public CardAuthResponse handleCardAuthRequest(@Valid CardDto cardDto) {
        final String newId = idGenerator.generateID();
        final CardTransaction cardTransaction = dtoToCardTransaction(newId, cardDto);
        kafkaProducerClient.produce(cardTransaction);

        return new CardAuthResponse(true, newId);
    }

    public CardTransaction dtoToCardTransaction(String transactionId, CardDto cardDto) {
        CardTransaction transaction = new CardTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setCardNumber(cardDto.getCardNumber());
        transaction.setCardExpiryDate(cardDto.getExpirationDate());
        transaction.setCardCvv(cardDto.getCvv());
        return transaction;
    }
}
