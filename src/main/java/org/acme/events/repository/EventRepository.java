package org.acme.events.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import org.acme.events.domain.EventEntity;
import org.hibernate.cfg.AvailableSettings;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@ApplicationScoped
public class EventRepository implements PanacheRepositoryBase<EventEntity, UUID> {
    public Collection<EventEntity> findEvents(int batchSize) {
        return find("scheduledFor <= ?1", Sort.by("scheduledFor").ascending(), Instant.now())
            .withLock(LockModeType.PESSIMISTIC_WRITE)
            .withHint(AvailableSettings.JAKARTA_LOCK_TIMEOUT, "-2")
            .range(0, batchSize)
            .list();
    }
}
