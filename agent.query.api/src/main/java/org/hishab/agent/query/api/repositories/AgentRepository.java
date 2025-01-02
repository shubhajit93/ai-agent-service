package org.hishab.agent.query.api.repositories;

import org.hishab.agent.query.api.model.agent.AgentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends MongoRepository<AgentDocument, String> {
    Optional<AgentDocument> findByAgentId(String agentId);

    Page<AgentDocument> findAgentDocumentsByAgentIdIn(List<String> agentIds, Pageable pageable);
}
