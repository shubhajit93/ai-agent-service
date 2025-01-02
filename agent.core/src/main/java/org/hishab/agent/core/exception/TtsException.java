package org.hishab.agent.core.exception;

public class TtsException extends IllegalStateException {

    public TtsException(String id) {
        super("Cannot perform operation because TtsDto [" + id + "] is already exist.");
    }

    public TtsException(Throwable throwable) {
        super(throwable.getMessage());
    }
}
