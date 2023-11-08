package org.acme.events.outbox;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.events.emitters.Emitters;
import org.acme.events.domain.EventEntity;
import org.acme.events.domain.FailedEventEntity;
import org.acme.events.repository.EventRepository;
import org.acme.events.repository.FailedEventRepository;

import java.util.Collection;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class Outbox {
    private final Emitters emitters;
    private final EventRepository eventRepository;
    private final FailedEventRepository failedEventRepository;
    private final OutboxConfig config;

    @Scheduled(every = "{acme.outbox.poll-interval}", identity = "outbox-poller")
    @Transactional
    public void poll() {
        Collection<EventEntity> events = eventRepository.findEvents(config.batchSize());
        log.debug("Found {} events to dispatch", events.size());

        events.forEach(event -> {
            try {
                emitters.findEmitterFor(event)
                    .send(event)
                    .toCompletableFuture()
                    .join();

                eventRepository.delete(event);
            } catch (Exception e) {
                log.info("Event processing failed [id: {}, topic: {}, retryCount: {}]",
                    event.getId(), event.getTopic(), event.getRetryCount());

                if (event.maxRetryDone(config.maxRetry())) {
                    log.warn("Event exceeded retry count [id: {}, topic: {}, retryCount: {}]",
                        event.getId(), event.getTopic(), event.getRetryCount());

                    eventRepository.delete(event);
                    failedEventRepository.persist(FailedEventEntity.from(event, "retry", "Exceeded retry count"));
                } else {
                    eventRepository.persist(event.forReschedule(config.retryExponent()));
                }
            }
        });
    }
}
