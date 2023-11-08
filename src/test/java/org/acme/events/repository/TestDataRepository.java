package org.acme.events.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.events.domain.TestData;

import java.util.UUID;

@ApplicationScoped
public class TestDataRepository implements PanacheRepositoryBase<TestData, UUID> {
}
