package org.hishab.agent.query.api.handlers.queries;

import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.hishab.agent.core.model.stt.Stt;
import org.hishab.agent.core.queries.CheckSttExistsQuery;
import org.hishab.agent.core.queries.FindSttDocumentByNameQuery;
import org.hishab.agent.query.api.model.stt.SttDocument;
import org.hishab.agent.query.api.queries.FindAllSttDocumentsQuery;
import org.hishab.agent.query.api.queries.FindSttDocumentByIdQuery;
import org.hishab.agent.query.api.repositories.SttRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
public class SttQueryHandler {

    private final SttRepository sttRepository;
    private final MongoTemplate mongoTemplate;

    @QueryHandler
    public List<SttDocument> handle(FindAllSttDocumentsQuery query) {
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
        return mongoTemplate.find(mongoQuery, SttDocument.class);
//        return sttRepository.findAll();
    }

    @QueryHandler
    public SttDocument handle(FindSttDocumentByIdQuery query) {
        return sttRepository.findById(query.getId()).orElse(null);
    }

    @QueryHandler
    public Stt handle(FindSttDocumentByNameQuery query) {
        var res = sttRepository.findByName(query.getName()).orElse(null);
        if (res == null) {
            return null;
        }
        return Stt.builder()
                .name(res.getName())
                .status(res.getStatus())
                .models(res.getModels())
                .createdAt(res.getCreatedAt())
                .modifiedAt(res.getModifiedAt())
                .build();
    }

    @QueryHandler
    public boolean handle(CheckSttExistsQuery query) {
        return sttRepository.existsByName(query.getName());
    }
} 