package org.hishab.agent.query.api.controller;

import lombok.AllArgsConstructor;
import org.hishab.agent.core.enums.TtsStatus;
import org.hishab.agent.query.api.dispatcher.TtsQueryDispatcher;
import org.hishab.agent.query.api.response.TtsListResponse;
import org.hishab.agent.query.api.response.TtsResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
@RequestMapping("/tts")
public class TtsController {

    private final TtsQueryDispatcher ttsQueryDispatcher;

    @GetMapping
    @Async("virtualThreadExecutor")
    public CompletableFuture<TtsListResponse> getAllTtsDocuments(
            @RequestParam(value = "sort_direction", required = false) String sortDirection,
            @RequestParam(value = "status", required = false) TtsStatus status,
            @RequestParam(value = "page_size") int pageSize,
            @RequestParam(value = "page_token", required = false) String pageToken
    ) {
        return ttsQueryDispatcher.findAllTtsDocuments(sortDirection, status, pageSize, pageToken);
    }

//    @GetMapping("/{id}")
//    public CompletableFuture<TtsResponse> getTtsDocumentById(@PathVariable String id) {
//        return ttsQueryDispatcher.findTtsDocumentById(id);
//    }

    @GetMapping("/name/{name}")
    public CompletableFuture<TtsResponse> getTtsDocumentByName(@PathVariable String name) {
        return ttsQueryDispatcher.findTtsDocumentByName(name);
    }
} 