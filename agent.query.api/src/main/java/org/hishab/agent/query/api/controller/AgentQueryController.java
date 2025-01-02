package org.hishab.agent.query.api.controller;

import lombok.AllArgsConstructor;
import org.hishab.agent.core.enums.AgentStatus;
import org.hishab.agent.core.exception.AgentException;
import org.hishab.agent.core.exception.AgentQueryException;
import org.hishab.agent.query.api.dispatcher.AgentQueryDispatcher;
import org.hishab.agent.query.api.response.AgentListResponse;
import org.hishab.agent.query.api.response.AgentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v2/ai-agents")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AgentQueryController {

    private final AgentQueryDispatcher agentQueryDispatcher;

//    @GetMapping("/by-id/{id}")
//    public CompletableFuture<AgentResponse> getAgentById(@PathVariable String id) {
//        return agentQueryDispatcher.findAgentById(id);
//    }


//    @GetMapping("/agentDtos/search")
//    public CompletableFuture<List> searchAgents(@RequestParam String searchTerm) {
//        return queryGateway.query(new SearchAgents(searchTerm), List.class);
//    }

    //    @GetMapping("/agents")
    @GetMapping
    @Async("virtualThreadExecutor")
    public CompletableFuture<AgentListResponse> fetchAgents(
            @RequestParam(value = "agent_ids", required = false) List<String> agentIds,
            @RequestParam(value = "agent_status", required = false) AgentStatus agentStatus,
            @RequestParam(value = "sort_direction", required = false) String sortDirection,
            @RequestParam(value = "page_size") int pageSize,
            @RequestParam(value = "page_token", required = false) String pageToken
    ) {
        System.out.println("Running on thread: " + Thread.currentThread());

        return agentQueryDispatcher.findAllAgents(agentIds, agentStatus, sortDirection, pageSize, pageToken);
    }

    @GetMapping("/{agent-id}")
    @Async("virtualThreadExecutor")
    public CompletableFuture<ResponseEntity<AgentResponse>> getAgentByAgentId(@PathVariable("agent-id") String agentId) throws AgentException, AgentQueryException {
        return agentQueryDispatcher.findAgentByAgentId(agentId)
                ;
    }
}
