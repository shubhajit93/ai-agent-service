package org.hishab.agent.cmd.api.controller;

import lombok.AllArgsConstructor;
import org.hishab.agent.cmd.api.dto.tts.TtsRequest;
import org.hishab.agent.cmd.api.service.impl.TtsServiceImpl;
import org.hishab.agent.core.dto.ApiResponse;
import org.hishab.agent.core.model.tts.Tts;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
@RequestMapping("/tts")
public class TtsController {

    private final TtsServiceImpl ttsService;

    @PostMapping("/create-tts")
    public CompletableFuture<ApiResponse<Tts>> createTts(@RequestBody TtsRequest ttsRequest) {
        return ttsService.createTts(ttsRequest);
    }

    @PutMapping("/update-tts/{name}")
    public CompletableFuture<ApiResponse<Tts>> updateTts(
            @RequestParam("name") String name,
            @RequestBody TtsRequest ttsRequest
    ) {
        return ttsService.updateTts(name, ttsRequest);
    }
}