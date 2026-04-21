package com.payment.adapters.schedulers;

import com.payment.adapters.producers.ProducerPixPaymentPendenteAdapter;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class OutboxScheduler {

    private final LockProvider lockProvider;
    private final ProducerPixPaymentPendenteAdapter producerPixPaymentPendenteAdapter;

    @NonBlocking
    @Scheduled(every = "5s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    public Uni<Void> process() {
        var lockConfig = new LockConfiguration(
                Instant.now(),
                "outboxScheduler",
                Duration.ofSeconds(50),
                Duration.ofSeconds(30)
        );

        return Uni.createFrom()
                .item(() -> lockProvider.lock(lockConfig))
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool()) // <-- lock em worker thread
                .onItem().transformToUni(lockOpt -> {
                    if (lockOpt.isEmpty()) {
                        log.debug("Lock não adquirido, outro nó está processando.");
                        return Uni.createFrom().voidItem();
                    }

                    var lock = lockOpt.get();
                    return producerPixPaymentPendenteAdapter.process()
                            .onTermination().invoke(() -> {
                                lock.unlock();
                                log.debug("Lock liberado.");
                            });
                });
    }
}