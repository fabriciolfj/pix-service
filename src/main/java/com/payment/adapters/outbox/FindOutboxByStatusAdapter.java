package com.payment.adapters.outbox;

import com.payment.persistences.entities.OutboxEventEntity;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;

@ApplicationScoped
public class FindOutboxByStatusAdapter {

    public Multi<OutboxEventEntity> getByStatus(final String status) {
        return OutboxEventEntity.find("status", status)
                .page(Page.ofSize(100))
                .list()
                .replaceIfNullWith(Collections::emptyList)
                .toMulti()
                .onItem()
                .disjoint()
                .map(value -> (OutboxEventEntity) value);
    }
}
