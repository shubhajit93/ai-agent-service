package org.hishab.agent.query.api.validation;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.hishab.agent.core.dto.ErrorResponse;
import org.hishab.agent.core.enums.TtsStatus;
import org.hishab.agent.core.exception.AgentException;
import org.hishab.agent.core.model.agent.AgentTts;
import org.hishab.agent.core.model.tts.Tts;
import org.hishab.agent.core.model.tts.Voice;
import org.hishab.agent.core.queries.FindTtsDocumentByNameQuery;
import org.hishab.agent.core.utils.StringRelatedUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TtsValidator {

    private final QueryGateway queryGateway;

    public TtsValidator(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    public void isValidTtsInformation(AgentTts ttsRequest, ValidationType validationType) throws AgentException {
        Map<String, String> errorDetails = new HashMap<>();
        if (validationType.equals(ValidationType.TTSCREATION) && ttsRequest == null) {
            errorDetails.put("data.tts", "AgentTts Information is missing");
        } else {
            if (StringRelatedUtils.isNullOrEmpty(ttsRequest.getProvider())) {
                errorDetails.put("data.tts.provider", "AgentTts provider is missing");
            } else {
                Tts result = queryGateway.query(
                        new FindTtsDocumentByNameQuery(ttsRequest.getProvider()),
                        ResponseTypes.instanceOf(Tts.class)
                ).join();

                if (result == null) {
                    errorDetails.put("data.tts.provider", "invalid provider");
                } else {
                    if (result.getStatus().equals(TtsStatus.INACTIVE)) {
                        errorDetails.put("data.tts.provider", "provider is inactive");
                    }
                    if (ttsRequest.getModelName() != null && !result.getModels().contains(ttsRequest.getModelName())) {
                        errorDetails.put("data.tts.model", "invalid model");
                    }
                    if (ttsRequest.getVoiceName() != null && ttsRequest.getVoiceId() != null) {
                        Voice voiceFromRequest = Voice.builder()
                                .id(ttsRequest.getVoiceId())
                                .name(ttsRequest.getVoiceName())
                                .build();
                        if (result.getVoices() != null && !result.getVoices().contains(voiceFromRequest)) {
                            errorDetails.put("data.tts.voice", "invalid voice");
                        }
                    }
                }
            }
        }

        if (!errorDetails.isEmpty()) {
            throw new AgentException(ErrorResponse.VALIDATION_ERROR, "TTS validation failed", errorDetails, null);
        }
    }
}