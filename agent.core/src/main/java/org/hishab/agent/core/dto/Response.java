package org.hishab.agent.core.dto;

public interface Response<T> {
    T getData();

    String getMessage();

    String getStatus();
}
