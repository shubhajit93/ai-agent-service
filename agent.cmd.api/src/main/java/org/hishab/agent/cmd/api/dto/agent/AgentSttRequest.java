package org.hishab.agent.cmd.api.dto.agent;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentSttRequest {

    @NotNull(message = "STT provider cannot be null")
    private String provider;
    private String model;
}
