package org.hishab.agent.cmd.api.validation;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.hishab.agent.cmd.api.dto.agent.AgentSttRequest;
import org.hishab.agent.core.dto.ErrorResponse;
import org.hishab.agent.core.enums.SttStatus;
import org.hishab.agent.core.exception.AgentException;
import org.hishab.agent.core.model.stt.Stt;
import org.hishab.agent.core.queries.FindSttDocumentByNameQuery;
import org.hishab.agent.core.utils.StringRelatedUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SttValidator {

    private final QueryGateway queryGateway;

    public SttValidator(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    public void isValidSttInformation(AgentSttRequest sttRequest, ValidationType validationType) throws AgentException {
        Map<String, String> errorDetails = new HashMap<>();
        if (validationType.equals(ValidationType.STTCREATION) && sttRequest == null) {
            errorDetails.put("data.stt", "Stt Information is missing");
        } else {
            if (StringRelatedUtils.isNullOrEmpty(sttRequest.getProvider())) {
                errorDetails.put("data.stt.provider", "Stt provider is missing");
            } else {
                Stt result = queryGateway.query(
                        new FindSttDocumentByNameQuery(sttRequest.getProvider()),
                        ResponseTypes.instanceOf(Stt.class)
                ).join();

                if (result == null) {
                    errorDetails.put("data.stt.provider", "invalid provider");
                } else {
                    if (!result.getName().equals(sttRequest.getProvider())) {
                        errorDetails.put("data.stt.provider", "invalid provider");
                    }
                    if (result.getStatus().equals(SttStatus.INACTIVE)) {
                        errorDetails.put("data.stt.provider", "provider is inactive");
                    }
                    if (sttRequest.getModel() != null && !result.getModels().contains(sttRequest.getModel())) {
                        errorDetails.put("data.stt.model", "invalid model");
                    }
                }
            }
        }

        if (!errorDetails.isEmpty()) {
            throw new AgentException(ErrorResponse.VALIDATION_ERROR, "STT validation failed", errorDetails, null);
        }
    }
}