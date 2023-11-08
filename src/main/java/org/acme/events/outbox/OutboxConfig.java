package org.acme.events.outbox;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Optional;

@ConfigMapping(prefix = "acme.outbox")
public interface OutboxConfig {
    @WithDefault("20")
    Integer batchSize();

    @WithDefault("3")
    Integer maxRetry();

    @WithDefault("2.0")
    Double retryExponent();
}
