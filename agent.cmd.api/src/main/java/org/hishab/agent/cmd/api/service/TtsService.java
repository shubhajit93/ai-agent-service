package org.hishab.agent.cmd.api.service;

import org.hishab.agent.cmd.api.dto.tts.TtsRequest;
import org.hishab.agent.core.dto.ApiResponse;
import org.hishab.agent.core.model.tts.Tts;

import java.util.concurrent.CompletableFuture;

public interface TtsService {
    CompletableFuture<ApiResponse<Tts>> createTts(TtsRequest sttRequest);

    CompletableFuture<ApiResponse<Tts>> updateTts(String name, TtsRequest sttRequest);
}
