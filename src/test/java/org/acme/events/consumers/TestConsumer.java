package org.acme.events.consumers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.acme.events.domain.TestData;
import org.acme.events.domain.EventEntity;
import org.acme.events.repository.TestDataRepository;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
@Slf4j
public class TestConsumer {
    @Inject
    TestDataRepository testDataRepository;

    private int count = 0;

    public void reset() {
        count = 0;
    }

    public int getCount() {
        return count;
    }

    @Incoming("topic_a")
    @Transactional
    public void handle(EventEntity notification) {
        log.info("Handling event [id: {}, payload: {}, count: {}]",
            notification.getId(), notification.getPayload(), count);

        TestData testData = new TestData();
        testData.setEventId(notification.getId());
        testData.setCorrelationId(notification.getCorrelationId());
        testDataRepository.persist(testData);

        Integer payload = notification.getPayloadContent();
        if (count < payload) {
            count++;
            throw new RuntimeException("Forcing event failure");
        }
    }
}
