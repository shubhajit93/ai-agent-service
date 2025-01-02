package org.hishab.agent.cmd.api.service;

import org.hishab.agent.cmd.api.dto.agent.AgentRequest;
import org.hishab.agent.core.dto.ApiResponse;
import org.hishab.agent.core.exception.AgentException;
import org.hishab.agent.core.model.agent.Agent;

import java.util.concurrent.CompletableFuture;

public interface AgentService {
    CompletableFuture<ApiResponse<Agent>> createAgent(AgentRequest agent) throws AgentException;

    CompletableFuture<ApiResponse<Agent>> updateAgent(String agentId, AgentRequest agent) throws AgentException;

    CompletableFuture<ApiResponse<Object>> deactivateAgent(String agentId) throws AgentException;
}