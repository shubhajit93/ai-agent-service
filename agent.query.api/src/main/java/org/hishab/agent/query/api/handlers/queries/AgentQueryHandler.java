package org.hishab.agent.query.api.handlers.queries;

import org.axonframework.queryhandling.QueryHandler;
import org.hishab.agent.query.api.model.agent.AgentDocument;
import org.hishab.agent.query.api.queries.FindAgentByAgentIdQuery;
import org.hishab.agent.query.api.queries.FindAgentByIdQuery;
import org.hishab.agent.query.api.queries.FindAllAgentsQuery;
import org.hishab.agent.query.api.repositories.AgentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class AgentQueryHandler {

    // Assuming you have a repository or service to fetch agentDtos
    private final AgentRepository agentRepository;
    private final MongoTemplate mongoTemplate;

    public AgentQueryHandler(AgentRepository agentRepository, MongoTemplate mongoTemplate) {
        this.agentRepository = agentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @QueryHandler
    public List<AgentDocument> handle(FindAllAgentsQuery query) {
        Sort.Direction direction = "desc".equalsIgnoreCase(query.getSortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Query mongoQuery = new Query();
        mongoQuery.limit(query.getPageSize());

        // Add sorting
        mongoQuery.with(Sort.by(direction, "createdAt"));

        // Add filters
        if (query.getAgentIds() != null) {
            mongoQuery.addCriteria(Criteria.where("agentId").in(query.getAgentIds()));
        }
        if (query.getAgentStatus() != null) {
            mongoQuery.addCriteria(Criteria.where("agentStatus").is(query.getAgentStatus()));
        } else {
            mongoQuery.addCriteria(Criteria.where("agentStatus").is("ACTIVE"));
        }
        // If pageToken is provided, add it as a filter for cursor-based pagination
        if (query.getPageToken() != null) {
            mongoQuery.addCriteria(Criteria.where("createdAt").gt(Instant.parse(query.getPageToken())));
        }

        return mongoTemplate.find(mongoQuery, AgentDocument.class);
    }

    @QueryHandler
    public AgentDocument handle(FindAgentByIdQuery query) {
        // Fetch and return the agentDto by id
        return agentRepository.findById(query.getId()).orElse(null);
    }

    @QueryHandler
    public AgentDocument handle(FindAgentByAgentIdQuery query) {
        // Fetch and return the agent by agentId
        return agentRepository.findByAgentId(query.getAgentId()).orElse(null);
    }
}
