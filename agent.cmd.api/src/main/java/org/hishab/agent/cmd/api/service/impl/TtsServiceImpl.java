package org.hishab.agent.cmd.api.service.impl;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.hishab.agent.cmd.api.commands.CreateTtsCommand;
import org.hishab.agent.cmd.api.commands.UpdateTtsCommand;
import org.hishab.agent.cmd.api.dto.tts.TtsRequest;
import org.hishab.agent.cmd.api.service.TtsService;
import org.hishab.agent.core.dto.ApiResponse;
import org.hishab.agent.core.enums.TtsStatus;
import org.hishab.agent.core.model.tts.Tts;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class TtsServiceImpl implements TtsService {

    private final CommandGateway commandGateway;

    @Override
    public CompletableFuture<ApiResponse<Tts>> createTts(TtsRequest ttsRequest) {
        String id = UUID.randomUUID().toString();
        return commandGateway.send(CreateTtsCommand.builder()
                        .id(id)
                        .tts(Tts.builder()
                                .name(ttsRequest.getName())
                                .status(
                                        Arrays.stream(TtsStatus.values())
                                                .anyMatch(status -> status.name().equals(ttsRequest.getStatus().name()))
                                                ? ttsRequest.getStatus()
                                                : TtsStatus.ACTIVE
                                )
                                .models(ttsRequest.getModels())
                                .voices(ttsRequest.getVoices())
                                .voiceTemperature(ttsRequest.getVoiceTemperature())
                                .createdAt(OffsetDateTime.now(ZoneId.of("UTC")).toInstant())
                                .modifiedAt(OffsetDateTime.now(ZoneId.of("UTC")).toInstant())
                                .build())
                        .build())
                .thenApply(result -> new ApiResponse<>((Tts) result, "TTS created successfully", "SUCCESS", null));
    }

    @Override
    public CompletableFuture<ApiResponse<Tts>> updateTts(String name, TtsRequest ttsRequest) {
        return commandGateway.send(UpdateTtsCommand.builder()
                        .id(UUID.randomUUID().toString())
                        .tts(Tts.builder()
                                .name(name)
                                .models(ttsRequest.getModels())
                                .status(ttsRequest.getStatus() != null ?
                                        (Arrays.stream(TtsStatus.values())
                                                .anyMatch(status -> status.name().equals(ttsRequest.getStatus().name()))
                                                ? ttsRequest.getStatus()
                                                : TtsStatus.ACTIVE) : null
                                )
                                .voices(ttsRequest.getVoices())
                                .voiceTemperature(ttsRequest.getVoiceTemperature())
                                .build())
                        .build())
                .thenApply(result -> new ApiResponse<>((Tts) result, "TTS updated successfully", "SUCCESS", null));
    }
} 