package org.acme.events.emitters;

import org.acme.events.domain.EventEntity;

import java.util.concurrent.CompletionStage;

public interface EventEmitter {
    boolean canHandle(EventEntity event);

    CompletionStage<Void> send(EventEntity event);
}
