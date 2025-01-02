package org.hishab.agent.core.enums;

public enum ResponseStatus {
    SUCCESS("success"), ERROR("error"), FAILURE("failure");
    public String value;

    ResponseStatus(String value) {
        this.value = value;
    }
}
