package org.acme.events.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.events.domain.FailedEventEntity;
import org.acme.events.domain.TestData;
import org.acme.events.repository.FailedEventRepository;
import org.acme.events.repository.TestDataRepository;

import java.util.UUID;

@ApplicationScoped
public class TestDataService {
    @Inject
    TestDataRepository testDataRepository;

    @Inject
    FailedEventRepository failedEventRepository;

    @Transactional
    public void cleanTestData() {
        testDataRepository.deleteAll();
        testDataRepository.flush();
    }

    @Transactional
    public TestData getTestData(UUID eventId) {
        return testDataRepository.find("eventId = ?1", eventId)
            .firstResult();
    }

    @Transactional
    public FailedEventEntity getFailedEvent(UUID eventId) {
        return failedEventRepository.find("eventId = ?1", eventId)
            .firstResult();
    }
}
