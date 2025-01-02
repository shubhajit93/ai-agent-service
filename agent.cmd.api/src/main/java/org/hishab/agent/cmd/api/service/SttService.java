package org.hishab.agent.cmd.api.service;

import org.hishab.agent.cmd.api.dto.stt.SttRequest;
import org.hishab.agent.core.dto.ApiResponse;
import org.hishab.agent.core.model.stt.Stt;

import java.util.concurrent.CompletableFuture;

public interface SttService {
    CompletableFuture<ApiResponse<Stt>> createStt(SttRequest sttRequest);

    CompletableFuture<ApiResponse<Stt>> updateStt(String modelName, SttRequest sttRequest);
}
