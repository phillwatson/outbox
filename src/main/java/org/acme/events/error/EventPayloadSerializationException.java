package org.acme.events.error;

public class EventPayloadSerializationException extends RuntimeException {
    public EventPayloadSerializationException(String className, Throwable cause) {
        super(String.format("Failed to serialize payload [class: %1$s]", className), cause);
    }

    public EventPayloadSerializationException(Object payload, Throwable cause) {
        this(payload.getClass().getName(), cause);
    }
}
