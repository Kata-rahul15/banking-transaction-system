package com.example.bank.service;

import com.example.bank.dto.TransactionEventDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final String topic;

    public EventPublisher(KafkaTemplate<String,Object> kafkaTemplate,
                          @Value("${bank.kafka.topic.transactions}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(TransactionEventDto event) {
        try {
            kafkaTemplate.send(topic, String.valueOf(event.getTransactionId()), event);
        } catch (Exception e) {
            System.out.println("Kafka not running, skipping event publish");
        }
    }
}