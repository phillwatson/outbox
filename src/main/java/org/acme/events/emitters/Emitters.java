package org.acme.events.emitters;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.acme.events.domain.EventEntity;

@ApplicationScoped
public class Emitters {
    @Inject @Any
    Instance<EventEmitter> emitters;

    public EventEmitter findEmitterFor(EventEntity eventEntity) {
        return emitters.stream()
            .filter(emitter -> emitter.canHandle(eventEntity))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Unable to find event emitter for topic " + eventEntity.getTopic()));
    }
}
