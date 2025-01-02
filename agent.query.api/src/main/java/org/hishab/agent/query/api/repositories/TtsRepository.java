package org.hishab.agent.query.api.repositories;

import org.hishab.agent.query.api.model.tts.TtsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TtsRepository extends MongoRepository<TtsDocument, String> {
    Optional<TtsDocument> findByName(String name);
} 