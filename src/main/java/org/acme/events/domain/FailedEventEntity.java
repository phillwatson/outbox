package org.acme.events.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(schema = "outbox", name = "message_hospital")
@Builder(access = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FailedEventEntity {
    public static FailedEventEntity from(EventEntity eventEntity,
                                         String reason, String cause) {
        return FailedEventEntity.builder()
            .eventId(eventEntity.getId())
            .correlationId(eventEntity.getCorrelationId())
            .type(eventEntity.getType())
            .topic(eventEntity.getTopic())
            .payload(eventEntity.getPayload())
            .createdAt(eventEntity.getCreatedAt())
            .retryCount(eventEntity.getRetryCount())
            .reason(reason)
            .cause(cause)
            .build();
    }

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="event_id")
    private UUID eventId;

    @Column(name="correlation_id")
    private String correlationId;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String payload;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "reason")
    private String reason;

    @Column(name = "cause")
    private String cause;
}
