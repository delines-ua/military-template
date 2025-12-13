package ua.edu.viti.military.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ActionLogEvent extends ApplicationEvent {
    private final String message;
    private final String user;

    public ActionLogEvent(Object source, String message, String user) {
        super(source);
        this.message = message;
        this.user = user;
    }
}