package org.hishab.agent.cmd.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.hishab.agent.cmd.api.dto.agent.AgentLlmRequest;
import org.hishab.agent.cmd.api.dto.llm.LlmResponseWrapper;
import org.hishab.agent.core.dto.ErrorResponse;
import org.hishab.agent.core.exception.AgentException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class LlmServiceImpl {
    private final WebClient llmWebClient;

    public LlmServiceImpl(@Qualifier("llmWebClient") WebClient llmWebClient) {
        this.llmWebClient = llmWebClient;
    }

    public Mono<LlmResponseWrapper> createLlm(AgentLlmRequest llmRequest) {
        return this.llmWebClient.post()
                .uri("/v2/verbex-llms")
                .bodyValue(llmRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("4xx Client Error from llm service for llm model {}", llmRequest.getModelName());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new AgentException(
                                    ErrorResponse.VALIDATION_ERROR,
                                    "Client error occurred: " + errorBody, null, null
                            )));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("5xx Server Error from llm service for llm Model {}", llmRequest.getModelName());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new AgentException(
                                    ErrorResponse.INTERNAL_ERROR,
                                    "Server error occurred: " + errorBody, null, null
                            )));
                })
                .bodyToMono(LlmResponseWrapper.class)
                .doOnNext(responseBody -> log.info("Response Body: {}", responseBody)) // Log the response body
                .onErrorResume(Exception.class, ex -> {
                    log.error("Unexpected error from llm service for llm model {}", llmRequest.getModelName(), ex);
                    return Mono.error(new AgentException(
                            ErrorResponse.INTERNAL_ERROR,
                            "Unexpected error: " + ex.getMessage(), null, null
                    ));
                });
    }
}
