package com.payment.adapters.createpayment;

import com.payment.models.PixPayment;
import com.payment.persistences.entities.PixPaymentEntity;
import com.payment.usecases.create.CreatePaymentGateway;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class CreatePaymentAdapter implements CreatePaymentGateway {

    @Override
    @WithTransaction
    public Uni<PixPayment> process(PixPayment payment) {
        return Uni.createFrom().item(payment)
                .map(PixPaymentEntityMapper::toEntity)
                .flatMap(PanacheEntityBase::persistAndFlush)
                .map(obj -> (PixPaymentEntity) obj)
                .invoke(entity -> log.info("payment save success {}", entity.getId()))
                .replaceWith(payment);
    }
}
