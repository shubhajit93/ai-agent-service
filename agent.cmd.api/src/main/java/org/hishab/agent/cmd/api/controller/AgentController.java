package org.hishab.agent.cmd.api.controller;

import org.hishab.agent.cmd.api.dto.agent.AgentRequest;
import org.hishab.agent.cmd.api.service.AgentService;
import org.hishab.agent.core.dto.ApiResponse;
import org.hishab.agent.core.enums.ResponseStatus;
import org.hishab.agent.core.exception.AgentException;
import org.hishab.agent.core.model.agent.Agent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin(origins = "*")
public class AgentController {
    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/v2/ai-agents")
    @CrossOrigin(origins = "*")
    public CompletableFuture<ResponseEntity<ApiResponse<Agent>>> createPiyaAgent(
            @RequestBody AgentRequest agentRequest) throws AgentException {
        return agentService.createAgent(agentRequest)
                .thenApply(response -> {
                    if (ResponseStatus.ERROR.value.equals(response.getStatus())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(null, ex.getMessage(), ResponseStatus.ERROR.value, null)))
                ;
    }

    @PutMapping("/v2/ai-agents/{agent_id}")
    public CompletableFuture<ResponseEntity<ApiResponse<Agent>>> updateAgent(
            @RequestParam("agent_id") String agentId,
            @RequestBody AgentRequest agentRequest
    ) throws AgentException {
        return agentService.updateAgent(agentId, agentRequest)
                .thenApply(response -> {
                    if (ResponseStatus.ERROR.value.equals(response.getStatus())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(null, ex.getMessage(), ResponseStatus.ERROR.value, null)))
                ;
    }

    @DeleteMapping("/v2/ai-agents/{agent_id}")
    public CompletableFuture<ResponseEntity<ApiResponse<Object>>> deactivateAgent(
            @PathVariable("agent_id") String agentId
    ) throws AgentException {
        return agentService.deactivateAgent(agentId)
                .thenApply(response -> {
                    if (ResponseStatus.ERROR.value.equals(response.getStatus())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                    return ResponseEntity.ok(response);
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(null, ex.getMessage(), ResponseStatus.ERROR.value, null)))
                ;
    }

}
