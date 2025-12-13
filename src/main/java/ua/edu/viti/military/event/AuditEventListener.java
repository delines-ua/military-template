package ua.edu.viti.military.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditEventListener {

    @Async // <--- Виконується в окремому потоці!
    @EventListener
    public void handleActionEvent(ActionLogEvent event) {
        // Імітуємо важку роботу (відправка пошти, запис в складний лог)
        try {
            Thread.sleep(1000); // Затримка 1 сек
            log.info("===> [AUDIT EVENT] User: {}, Action: {}", event.getUser(), event.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}