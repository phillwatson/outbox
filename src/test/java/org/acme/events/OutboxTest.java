package org.acme.events;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.acme.events.consumers.TestConsumer;
import org.acme.events.domain.EventEntity;
import org.acme.events.domain.FailedEventEntity;
import org.acme.events.domain.Topic;
import org.acme.events.outbox.OutboxConfig;
import org.acme.events.service.TestDataService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Slf4j
public class OutboxTest {
    @Inject
    EventSender eventSender;

    @Inject
    TestConsumer testConsumer;

    @Inject
    TestDataService testDataService;

    @Inject
    OutboxConfig config;

    @Test
    public void testRetry_toSuccess() {
        testDataService.cleanTestData();
        testConsumer.reset();

        // given: an event that will fail for max-retry times - and then succeed
        EventEntity eventEntity = eventSender.sendEvent(Topic.TOPIC_A, config.maxRetry());

        Awaitility.await()
            .pollInterval(Duration.ofSeconds(2))
            .atMost(Duration.ofSeconds(30))
            .untilAsserted(() -> {
                // then: the event will eventually complete
                assertNotNull(testDataService.getTestData(eventEntity.getId()));
            });

        // and: the consumer was called max-retry times
        assertEquals(config.maxRetry(), testConsumer.getCount());

        // and: no failure is recorded
        assertNull(testDataService.getFailedEvent(eventEntity.getId()));
    }

    @Test
    public void testRetry_toFailure() {
        testDataService.cleanTestData();
        testConsumer.reset();

        // given: an event that will fail for more than max-retry times
        EventEntity eventEntity = eventSender.sendEvent(Topic.TOPIC_A, config.maxRetry() + 2);

        Awaitility.await()
            .pollInterval(Duration.ofSeconds(2))
            .atMost(Duration.ofSeconds(30))
            .untilAsserted(() -> {
                // then: the event is recorded as a failure
                FailedEventEntity failedEvent = testDataService.getFailedEvent(eventEntity.getId());
                assertNotNull(failedEvent);

                // and: the event was retried the max times
                assertEquals(config.maxRetry(), failedEvent.getRetryCount());
            });

        // and: the consumer was called more than max-retry times
        // note: the consumer will be called once for initial attempt and then max-retry times
        assertEquals(config.maxRetry() + 1, testConsumer.getCount());

        // and: no test data was persisted
        assertNull(testDataService.getTestData(eventEntity.getId()));
    }
}
