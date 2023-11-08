package org.acme.events.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.*;
import lombok.*;
import org.acme.events.error.EventPayloadDeserializationException;
import org.acme.events.error.EventPayloadSerializationException;

import java.time.Instant;
import java.util.*;

@Entity
@Table(schema = "outbox", name = "events")
@Builder(access = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EventEntity {
    public static EventEntity forInitialSubmission(Topic topic, Object payload) {
        try {
            return EventEntity.builder()
                .correlationId(UUID.randomUUID().toString())
                .topic(topic.getTopicName())
                .type(payload.getClass().getName())
                .payload(MapperFactory.serialize(payload))
                .createdAt(Instant.now())
                .scheduledFor(Instant.now())
                .retryCount(0)
                .build();
        } catch (JsonProcessingException e) {
            throw new EventPayloadSerializationException(payload, e);
        }
    }

    @Transient
    public EventEntity forReschedule(double exponent) {
        retryCount++;
        long delay = (long)Math.ceil(Math.pow(exponent, retryCount));
        scheduledFor = Instant.now().plusSeconds(delay);
        return this;
    }

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="correlation_id")
    private String correlationId;

    @Column(nullable = false)
    private String topic;

    // the class of the payload content
    @Column(nullable = false)
    private String type;

    // the JSON serialised form of the event payload
    @Column(nullable = false)
    private String payload;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "scheduled_for", nullable = false)
    private Instant scheduledFor;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Transient
    public boolean maxRetryDone(int maxAttempts) {
        return retryCount >= maxAttempts;
    }

    @Transient
    private transient Object payloadContent;

    @Transient
    public <T> T getPayloadContent() {
        if ((payloadContent == null) && (payload != null)) {
            try {
                payloadContent = MapperFactory.deserialize(payload, Class.forName(type));
            } catch (JsonProcessingException | ClassNotFoundException e) {
                throw new EventPayloadDeserializationException(type, e);
            }
        }
        return (T) payloadContent;
    }
}
