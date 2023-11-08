package org.acme.events.emitters;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.events.domain.Topic;
import org.acme.events.domain.EventEntity;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.Objects;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class TestEmitter implements EventEmitter {
    @Inject
    @Channel("topic_a")
    private Emitter<EventEntity> emitter;

    private int count = 0;

    @Override
    public boolean canHandle(EventEntity event) {
        return Objects.equals(event.getTopic(), Topic.TOPIC_A.getTopicName());
    }

    @Override
    public CompletionStage<Void> send(EventEntity event) {
        count++;
        return emitter.send(event);
    }

    public int getCount() {
        return count;
    }
}
