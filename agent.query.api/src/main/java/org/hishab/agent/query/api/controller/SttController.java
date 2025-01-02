package org.hishab.agent.query.api.controller;

import lombok.AllArgsConstructor;
import org.hishab.agent.core.enums.SttStatus;
import org.hishab.agent.query.api.dispatcher.SttQueryDispatcher;
import org.hishab.agent.query.api.response.SttListResponse;
import org.hishab.agent.query.api.response.SttResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
@RequestMapping("/stt")
public class SttController {

    private final SttQueryDispatcher sttQueryDispatcher;

    @GetMapping
    @Async("virtualThreadExecutor")
    public CompletableFuture<SttListResponse> getAllSttDocuments(
            @RequestParam(value = "sort_direction", required = false) String sortDirection,
            @RequestParam(value = "status", required = false) SttStatus status,
            @RequestParam(value = "page_size") int pageSize,
            @RequestParam(value = "page_token", required = false) String pageToken
    ) {
//        var res = sttQueryDispatcher.findAllSttDocuments().join();
        return sttQueryDispatcher.findAllSttList(sortDirection, status, pageSize, pageToken);
    }

//    @GetMapping("/{id}")
//    public CompletableFuture<SttResponse> getSttDocumentById(@PathVariable String id) {
//        return sttQueryDispatcher.findSttDocumentById(id);
//    }

    @GetMapping("/name/{name}")
    @Async("virtualThreadExecutor")
    public CompletableFuture<SttResponse> getSttDocumentByName(@PathVariable String name) {
        return sttQueryDispatcher.findSttByName(name);
    }

//    @DeleteMapping("/{id}")
//    public CompletableFuture<Void> softDeleteSttDocument(@PathVariable String id) {
//        return sttQueryDispatcher.softDeleteSttDocument(id);
//    }
} 