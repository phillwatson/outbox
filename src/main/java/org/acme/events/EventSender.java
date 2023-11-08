package org.acme.events;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.acme.events.domain.EventEntity;
import org.acme.events.domain.Topic;
import org.acme.events.repository.EventRepository;

@ApplicationScoped
@RequiredArgsConstructor
public class EventSender {
    private final EventRepository eventRepository;

    @Transactional
    public EventEntity sendEvent(Topic topic, Object payload) {
        return sendEvent(EventEntity.forInitialSubmission(topic, payload));
    }

    private EventEntity sendEvent(EventEntity eventEntity) {
        eventRepository.persist(eventEntity);
        return eventEntity;
    }
}
