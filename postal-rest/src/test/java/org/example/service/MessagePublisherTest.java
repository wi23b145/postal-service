package org.example.service;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.Mockito.*;

class MessagePublisherTest {

    @Test
    void publishLetter_sendsToLetterQueue() {
        RabbitTemplate tpl = mock(RabbitTemplate.class);
        MessagePublisher publisher = new MessagePublisher(tpl, "letter.q", "pack.q");

        UUID id = UUID.randomUUID();
        publisher.publishLetter(id);

        verify(tpl, times(1)).convertAndSend("letter.q", id.toString());
        verifyNoMoreInteractions(tpl);
    }

    @Test
    void publishPackage_sendsToPackQueue() {
        RabbitTemplate tpl = mock(RabbitTemplate.class);
        MessagePublisher publisher = new MessagePublisher(tpl, "letter.q", "pack.q");

        UUID id = UUID.randomUUID();
        publisher.publishPackage(id);

        verify(tpl, times(1)).convertAndSend("pack.q", id.toString());
        verifyNoMoreInteractions(tpl);
    }
}
