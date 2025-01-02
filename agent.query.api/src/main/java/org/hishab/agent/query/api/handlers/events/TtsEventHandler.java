package org.hishab.agent.query.api.handlers.events;

import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.hishab.agent.core.events.tts.TtsCreatedEvent;
import org.hishab.agent.core.events.tts.TtsUpdatedEvent;
import org.hishab.agent.query.api.model.tts.TtsDocument;
import org.hishab.agent.query.api.repositories.TtsRepository;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TtsEventHandler {

    private final TtsRepository ttsRepository;
    private final ThreadPoolTaskExecutor virtualThreadExecutor;

    @EventHandler
    public void on(TtsCreatedEvent event) {
        virtualThreadExecutor.execute(() -> {
            TtsDocument ttsDocument = TtsDocument.builder()
                    .id(event.getId())
                    .name(event.getTts().getName())
                    .status(event.getTts().getStatus())
                    .voices(event.getTts().getVoices())
                    .voiceTemperature(event.getTts().getVoiceTemperature())
                    .models(event.getTts().getModels())
                    .createdAt(event.getTts().getCreatedAt())
                    .modifiedAt(event.getTts().getModifiedAt())
                    .build();
            ttsRepository.save(ttsDocument);
        });
    }

    @EventHandler
    public void on(TtsUpdatedEvent event) {
        virtualThreadExecutor.execute(() -> {
            TtsDocument ttsDocument = ttsRepository.findByName(event.getTts().getName()).orElseThrow(() -> new RuntimeException("TTS not found"));
            if (event.getTts().getStatus() != null) {
                ttsDocument.setStatus(event.getTts().getStatus());
            }
            // want to check if tts.getVoices() not contains event.getTts().getVoices() then add it
            event.getTts().getVoices().forEach(voice -> {
                if (!ttsDocument.getVoices().contains(voice)) {
                    ttsDocument.getVoices().add(voice);
                }
            });
            if (event.getTts().getModels() != null && !event.getTts().getModels().isEmpty()) {
                // want to check if ttsDocument.getModels() not contains event.getStt().getModels() then add it
                event.getTts().getModels().forEach(model -> {
                    if (!ttsDocument.getModels().contains(model)) {
                        ttsDocument.getModels().add(model);
                    }
                });
            }
            if (event.getTts().getVoiceTemperature() != ttsDocument.getVoiceTemperature()) {
                ttsDocument.setVoiceTemperature(event.getTts().getVoiceTemperature());
            }
            ttsDocument.setModifiedAt(event.getTts().getModifiedAt());
            ttsRepository.save(ttsDocument);
        });
    }
} 