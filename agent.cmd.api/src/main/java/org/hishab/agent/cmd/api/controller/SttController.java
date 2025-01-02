package org.hishab.agent.cmd.api.controller;

import org.hishab.agent.cmd.api.dto.stt.SttRequest;
import org.hishab.agent.cmd.api.service.SttService;
import org.hishab.agent.core.dto.ApiResponse;
import org.hishab.agent.core.model.stt.Stt;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
public class SttController {
    private final SttService sttService;

    public SttController(SttService sttService) {
        this.sttService = sttService;
    }

    @PostMapping("/create-stt")
    public CompletableFuture<ApiResponse<Stt>> createStt(@RequestBody SttRequest sttRequest) {
        return sttService.createStt(sttRequest);
    }

    @PutMapping("/update-stt/{name}")
    public CompletableFuture<ApiResponse<Stt>> updateStt(
            @RequestParam("name") String name,
            @RequestBody SttRequest sttRequest
    ) {
        return sttService.updateStt(name, sttRequest);
    }
}
