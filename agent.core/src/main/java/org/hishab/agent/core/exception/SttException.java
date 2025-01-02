package org.hishab.agent.core.exception;

public class SttException extends IllegalStateException {

    public SttException(String id) {
        super("Cannot perform operation because sttDto [" + id + "] is already exist.");
    }

    public SttException(Throwable throwable) {
        super(throwable.getMessage());
    }
}
