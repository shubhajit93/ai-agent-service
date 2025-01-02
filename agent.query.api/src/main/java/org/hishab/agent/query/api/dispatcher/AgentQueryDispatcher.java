package org.hishab.agent.query.api.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.hishab.agent.core.enums.AgentStatus;
import org.hishab.agent.core.enums.ResponseStatus;
import org.hishab.agent.core.exception.AgentException;
import org.hishab.agent.core.exception.AgentQueryException;
import org.hishab.agent.core.model.agent.Agent;
import org.hishab.agent.query.api.mapper.AgentMapper;
import org.hishab.agent.query.api.model.agent.AgentDocument;
import org.hishab.agent.query.api.queries.FindAgentByAgentIdQuery;
import org.hishab.agent.query.api.queries.FindAgentByIdQuery;
import org.hishab.agent.query.api.queries.FindAllAgentsQuery;
import org.hishab.agent.query.api.repositories.SttRepository;
import org.hishab.agent.query.api.response.AgentListResponse;
import org.hishab.agent.query.api.response.AgentResponse;
import org.hishab.agent.query.api.validation.SttValidator;
import org.hishab.agent.query.api.validation.TtsValidator;
import org.hishab.agent.query.api.validation.ValidationType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AgentQueryDispatcher {

    private final QueryGateway queryGateway;
    private final SttValidator sttValidator;
    private final TtsValidator ttsValidator;

    public AgentQueryDispatcher(QueryGateway queryGateway, SttRepository sttRepository, SttValidator sttValidator, TtsValidator ttsValidator) {
        this.queryGateway = queryGateway;
        this.sttValidator = sttValidator;
        this.ttsValidator = ttsValidator;
    }

    public CompletableFuture<AgentListResponse> findAllAgents(List<String> agentIds, AgentStatus agentStatus, String sortDirection, int pageSize, String pageToken) {
        FindAllAgentsQuery query = new FindAllAgentsQuery(agentIds, agentStatus, sortDirection, pageSize, pageToken);
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(AgentDocument.class))
                .thenApply(agentDocuments -> {
                    List<Agent> agents = agentDocuments.stream()
                            .map(AgentMapper.INSTANCE::toAgent)
                            .collect(Collectors.toList());

                    String nextPageToken = agents.isEmpty() ? null : agents.get(agents.size() - 1).getCreatedAt().toString();
                    assert nextPageToken != null;
                    return AgentListResponse.builder()
                            .data(agents)
                            .nextPageToken(nextPageToken)
                            .message("Successfully Data Fetched")
                            .status(ResponseStatus.SUCCESS.value)
                            .errorDetails(null)
                            .build();
                });
    }

    public CompletableFuture<AgentResponse> findAgentById(String id) {
        return queryGateway.query(new FindAgentByIdQuery(id), ResponseTypes.instanceOf(AgentDocument.class))
                .thenApply(r -> new AgentResponse(r, "successFully Data Fetched", ResponseStatus.SUCCESS.value, null));
    }

    public CompletableFuture<ResponseEntity<AgentResponse>> findAgentByAgentId(String agentId) throws AgentException, AgentQueryException {
        var agentDocument = queryGateway.query(new FindAgentByAgentIdQuery(agentId), ResponseTypes.instanceOf(AgentDocument.class))
                .join();
        if (agentDocument == null) {
            return CompletableFuture.completedFuture(ResponseEntity.status(404).body(new AgentResponse(null, "Agent not found", ResponseStatus.ERROR.value, null)));
        }
        sttValidator.isValidSttInformation(agentDocument, ValidationType.STTCHECK);
        Agent agent = AgentMapper.INSTANCE.toAgent(agentDocument);
        return CompletableFuture.completedFuture(ResponseEntity.ok(new AgentResponse(agent, "Successfully Data Fetched", ResponseStatus.SUCCESS.value, null)));
    }

    private Agent convertToAgent(AgentDocument agentDocument) {
        return Agent.builder()
                .agentId(agentDocument.getId()) // Map the id from AgentDocument
                .agentName(agentDocument.getAgentName())
                .agentStatus(agentDocument.getAgentStatus())
                .tts(agentDocument.getTts())
                .stt(agentDocument.getStt())
                .enableUserInterruptions(agentDocument.isEnableUserInterruptions())
                .minimumSpeechDurationForInterruptions(agentDocument.getMinimumSpeechDurationForInterruptions())
                .minimumWordsBeforeInterruption(agentDocument.getMinimumWordsBeforeInterruption())
                .waitTimeBeforeDetectingEndOfSpeech(agentDocument.getWaitTimeBeforeDetectingEndOfSpeech())
                .ambientSound(agentDocument.getAmbientSound())
                .ambientSoundVolume(agentDocument.getAmbientSoundVolume())
                .webhookUrl(agentDocument.getWebhookUrl())
                .endCallAfterSilenceSeconds(agentDocument.getEndCallAfterSilenceSeconds())
                .maxCallDurationSeconds(agentDocument.getMaxCallDurationSeconds())
                .welcomeMessage(agentDocument.getWelcomeMessage())
                .voicemailDetectionTimeoutSeconds(agentDocument.getVoicemailDetectionTimeoutSeconds())
                .createdAt(agentDocument.getCreatedAt())
                .modifiedAt(agentDocument.getModifiedAt())
                .build();
    }

    private Instant generateNextPageToken(AgentDocument lastDocument) {
        return lastDocument.getCreatedAt();
    }
}
