package org.hishab.agent.query.api.handlers.queries;

import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.hishab.agent.core.model.tts.Tts;
import org.hishab.agent.core.queries.FindTtsDocumentByNameQuery;
import org.hishab.agent.query.api.model.tts.TtsDocument;
import org.hishab.agent.query.api.queries.FindAllTtsDocumentsQuery;
import org.hishab.agent.query.api.queries.FindTtsDocumentByIdQuery;
import org.hishab.agent.query.api.repositories.TtsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
public class TtsQueryHandler {

    private final TtsRepository ttsRepository;
    private final MongoTemplate mongoTemplate;

    @QueryHandler
    public List<TtsDocument> handle(FindAllTtsDocumentsQuery query) {
        Sort.Direction direction = "desc".equalsIgnoreCase(query.getSortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Query mongoQuery = new Query();
        mongoQuery.limit(query.getPageSize());

        // Add sorting
        mongoQuery.with(Sort.by(direction, "createdAt"));
        if (query.getStatus() != null) {
            mongoQuery.addCriteria(Criteria.where("status").is(query.getStatus()));
        }
        // If pageToken is provided, add it as a filter for cursor-based pagination
        if (query.getPageToken() != null) {
            mongoQuery.addCriteria(Criteria.where("createdAt").gt(Instant.parse(query.getPageToken())));
        }
//        return ttsRepository.findAll();
        return mongoTemplate.find(mongoQuery, TtsDocument.class);
    }

    @QueryHandler
    public TtsDocument handle(FindTtsDocumentByIdQuery query) {
        return ttsRepository.findById(query.getId()).orElse(null);
    }

    @QueryHandler
    public Tts handle(FindTtsDocumentByNameQuery query) {
        var res = ttsRepository.findByName(query.getName()).orElse(null);
        if (res == null) {
            return null;
        }
        return Tts.builder()
                .name(res.getName())
                .status(res.getStatus())
                .models(res.getModels())
                .voices(res.getVoices())
                .voiceTemperature(res.getVoiceTemperature())
                .createdAt(res.getCreatedAt())
                .modifiedAt(res.getModifiedAt())
                .build();
    }
} 