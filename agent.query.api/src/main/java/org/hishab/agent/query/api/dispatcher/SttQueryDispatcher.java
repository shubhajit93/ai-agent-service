package org.hishab.agent.query.api.dispatcher;

import lombok.AllArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.hishab.agent.core.enums.ResponseStatus;
import org.hishab.agent.core.enums.SttStatus;
import org.hishab.agent.core.model.stt.Stt;
import org.hishab.agent.core.queries.FindSttDocumentByNameQuery;
import org.hishab.agent.query.api.mapper.SttMapper;
import org.hishab.agent.query.api.model.stt.SttDocument;
import org.hishab.agent.query.api.queries.FindAllSttDocumentsQuery;
import org.hishab.agent.query.api.repositories.SttRepository;
import org.hishab.agent.query.api.response.SttListResponse;
import org.hishab.agent.query.api.response.SttResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class SttQueryDispatcher {

    private final QueryGateway queryGateway;
    private final SttRepository sttRepository;

    public CompletableFuture<SttListResponse> findAllSttList(String sortDirection, SttStatus status, int pageSize, String pageToken) {
        FindAllSttDocumentsQuery query = new FindAllSttDocumentsQuery(sortDirection, status, pageSize, pageToken);
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(SttDocument.class))
                .thenApply(sttDocuments -> {
                    List<Stt> sttList = sttDocuments.stream()
                            .map(SttMapper.INSTANCE::toStt)
                            .toList();
                    return new SttListResponse(sttList, "successFully Data Fetched", ResponseStatus.SUCCESS.value, null);
                });
    }

//    public CompletableFuture<SttResponse> findSttDocumentById(String id) {
//        return queryGateway.query(new FindSttDocumentByIdQuery(id), ResponseTypes.instanceOf(Stt.class))
//                .thenApply(r -> new SttResponse(r, "successFully Data Fetched", ResponseStatus.SUCCESS.value, null));
//    }

    public CompletableFuture<SttResponse> findSttByName(String name) {
        return queryGateway.query(new FindSttDocumentByNameQuery(name), ResponseTypes.instanceOf(Stt.class))
                .thenApply(r -> new SttResponse(r, "Successfully Data Fetched", ResponseStatus.SUCCESS.value, null));
    }

    public CompletableFuture<Void> softDeleteSttDocument(String id) {
        return CompletableFuture.runAsync(() -> {
            SttDocument sttDocument = sttRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("STT document not found"));
            sttDocument.setStatus(SttStatus.INACTIVE);
            sttRepository.save(sttDocument);
        });
    }
}
