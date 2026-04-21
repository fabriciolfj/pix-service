package com.payment.adapters.getpayment;

import com.payment.models.OutboxEvent;
import com.payment.persistences.entities.OutboxEventEntity;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;

@ApplicationScoped
public class FindPixPaymentAdapter {

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
