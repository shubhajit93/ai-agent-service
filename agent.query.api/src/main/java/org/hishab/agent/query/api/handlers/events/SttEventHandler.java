package org.hishab.agent.query.api.handlers.events;

import org.axonframework.eventhandling.EventHandler;
import org.hishab.agent.core.enums.SttStatus;
import org.hishab.agent.core.events.stt.SttCreatedEvent;
import org.hishab.agent.core.events.stt.SttUpdatedEvent;
import org.hishab.agent.query.api.model.stt.SttDocument;
import org.hishab.agent.query.api.repositories.SttRepository;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class SttEventHandler {
    private final SttRepository sttRepository;
    private final ThreadPoolTaskExecutor virtualThreadExecutor;

    public SttEventHandler(SttRepository sttRepository, ThreadPoolTaskExecutor virtualThreadExecutor) {
        this.sttRepository = sttRepository;
        this.virtualThreadExecutor = virtualThreadExecutor;
    }

    @EventHandler
    public void on(SttCreatedEvent event) {
        virtualThreadExecutor.execute(() -> {
            SttDocument sttDocument = SttDocument.builder()
                    .id(event.getId())
                    .name(event.getStt().getName())
                    .models(event.getStt().getModels())
                    .status(event.getStt().getStatus() != null ? event.getStt().getStatus() : SttStatus.ACTIVE)
                    .createdAt(event.getStt().getCreatedAt())
                    .modifiedAt(event.getStt().getModifiedAt())
                    .build();
            sttRepository.save(sttDocument);
        });
    }

    @EventHandler
    public void on(SttUpdatedEvent event) {
        virtualThreadExecutor.execute(() -> {
            SttDocument sttDocument = sttRepository.findByName(event.getStt().getName()).orElseThrow(() -> new RuntimeException("STT not found"));
//        SttDocument sttDocument = sttRepository.findById(event.getId()).orElseThrow(() -> new RuntimeException("STT not found"));
            if (event.getStt().getStatus() != null) {
                sttDocument.setStatus(event.getStt().getStatus());
            }
            if (!event.getStt().getModels().isEmpty()) {
                // want to check if sttDocument.getModels() not contains event.getStt().getModels() then add it
                event.getStt().getModels().forEach(model -> {
                    if (!sttDocument.getModels().contains(model)) {
                        sttDocument.getModels().add(model);
                    }
                });
            }
            sttRepository.save(sttDocument);
        });
    }
}
