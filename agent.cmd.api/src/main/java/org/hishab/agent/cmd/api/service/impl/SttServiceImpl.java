package org.hishab.agent.cmd.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.hishab.agent.cmd.api.commands.CreateSttCommand;
import org.hishab.agent.cmd.api.commands.UpdateSttCommand;
import org.hishab.agent.cmd.api.dto.stt.SttRequest;
import org.hishab.agent.cmd.api.service.SttService;
import org.hishab.agent.core.dto.ApiResponse;
import org.hishab.agent.core.enums.ResponseStatus;
import org.hishab.agent.core.enums.SttStatus;
import org.hishab.agent.core.exception.SttException;
import org.hishab.agent.core.model.stt.Stt;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class SttServiceImpl implements SttService {

    private final CommandGateway commandGateway;

    public SttServiceImpl(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<ApiResponse<Stt>> createStt(SttRequest sttRequest) {
        //TODO need to apply validation
        String traceId = UUID.randomUUID().toString();
        return commandGateway.send(
                        CreateSttCommand.builder()
                                .id(traceId)
                                .stt(Stt.builder()
                                        .name(sttRequest.getName())
                                        .models(sttRequest.getModels())
                                        // check if provided sttRequest.getStatus() is found in SttStatus enum. if not set default value to ACTIVE
                                        .status(
                                                Arrays.stream(SttStatus.values())
                                                        .anyMatch(status -> status.name().equals(sttRequest.getStatus().name()))
                                                        ? sttRequest.getStatus()
                                                        : SttStatus.ACTIVE
                                        )
                                        .createdAt(OffsetDateTime.now(ZoneId.of("UTC")).toInstant())
                                        .modifiedAt(OffsetDateTime.now(ZoneId.of("UTC")).toInstant())
                                        .build())
                                .build())
                .thenApply(result -> new ApiResponse<>((Stt) result, "STT Created Successfully", ResponseStatus.SUCCESS.value, null))
                .exceptionally(ex -> {
                    log.error("Failed to create STT. Reason: {}", ex.getMessage());
                    throw new SttException(ex);
                });
    }

    @Override
    public CompletableFuture<ApiResponse<Stt>> updateStt(String name, SttRequest sttRequest) {
        return commandGateway.send(
                        UpdateSttCommand.builder()
                                .id(UUID.randomUUID().toString())
                                .stt(
                                        Stt.builder()
                                                .name(name)
                                                .models(sttRequest.getModels())
                                                // check if provided sttRequest.getStatus() is found in SttStatus enum. if not set default value to ACTIVE
                                                .status(
                                                        Arrays.stream(SttStatus.values())
                                                                .anyMatch(status -> status.name().equals(sttRequest.getStatus().name()))
                                                                ? sttRequest.getStatus()
                                                                : SttStatus.ACTIVE
                                                )
                                                .createdAt(OffsetDateTime.now(ZoneId.of("UTC")).toInstant())
                                                .modifiedAt(OffsetDateTime.now(ZoneId.of("UTC")).toInstant())
                                                .build())
                                .build()
                )
                .thenApply(result -> new ApiResponse<>((Stt) result, "STT Updated Successfully", ResponseStatus.SUCCESS.value, null))
                .exceptionally(ex -> {
                    log.error("Failed to update STT. Reason: {}", ex.getMessage());
                    throw new SttException(ex);
                });
    }
}
