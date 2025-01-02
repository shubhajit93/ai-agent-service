package org.hishab.agent.query.api.dispatcher;

import lombok.AllArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.hishab.agent.core.enums.TtsStatus;
import org.hishab.agent.core.model.tts.Tts;
import org.hishab.agent.core.queries.FindTtsDocumentByNameQuery;
import org.hishab.agent.query.api.model.tts.TtsDocument;
import org.hishab.agent.query.api.queries.FindAllTtsDocumentsQuery;
import org.hishab.agent.query.api.queries.FindTtsDocumentByIdQuery;
import org.hishab.agent.query.api.response.TtsListResponse;
import org.hishab.agent.query.api.response.TtsResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class TtsQueryDispatcher {

    private final QueryGateway queryGateway;

    public CompletableFuture<TtsListResponse> findAllTtsDocuments(String sortDirection, TtsStatus status, int pageSize, String pageToken) {
//        sortDirection, pageSize, pageToken
        FindAllTtsDocumentsQuery query = new FindAllTtsDocumentsQuery(sortDirection, status, pageSize, pageToken);
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(TtsDocument.class))
                .thenApply(r ->
                {
                    List<Tts> ttsList = r.stream()
                            .map(org.hishab.agent.query.api.mapper.TtsMapper.INSTANCE::toTts)
                            .toList();
                    return new TtsListResponse(ttsList, "Successfully fetched data", "SUCCESS", null);

                });
    }

    public CompletableFuture<TtsResponse> findTtsDocumentById(String id) {
        return queryGateway.query(new FindTtsDocumentByIdQuery(id), ResponseTypes.instanceOf(org.hishab.agent.core.model.tts.Tts.class))
                .thenApply(r -> new TtsResponse(r, "Successfully fetched data", "SUCCESS", null));
    }

    public CompletableFuture<TtsResponse> findTtsDocumentByName(String name) {
        return queryGateway.query(new FindTtsDocumentByNameQuery(name), ResponseTypes.instanceOf(org.hishab.agent.core.model.tts.Tts.class))
                .thenApply(r -> new TtsResponse(r, "Successfully fetched data", "SUCCESS", null));
    }
} 