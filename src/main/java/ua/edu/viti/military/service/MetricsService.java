package ua.edu.viti.military.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final Counter actionCounter;

    public MetricsService(MeterRegistry registry) {
        // Створюємо лічильник "military.actions.count"
        this.actionCounter = Counter.builder("military.actions.count")
                .description("Кількість виконаних операцій")
                .register(registry);
    }

    public void incrementActions() {
        actionCounter.increment();
    }
}