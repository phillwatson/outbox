package org.acme.events.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(schema = "outbox", name = "test_data")
@Data
@NoArgsConstructor
public class TestData {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="event_id")
    private UUID eventId;

    @Column(name="correlation_id")
    private String correlationId;
}
