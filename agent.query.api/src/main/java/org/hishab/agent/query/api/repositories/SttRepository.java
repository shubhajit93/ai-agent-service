package org.hishab.agent.query.api.repositories;

import org.hishab.agent.query.api.model.stt.SttDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SttRepository extends MongoRepository<SttDocument, String> {
    Optional<SttDocument> findByName(String name);

    boolean existsByName(String name);
}
