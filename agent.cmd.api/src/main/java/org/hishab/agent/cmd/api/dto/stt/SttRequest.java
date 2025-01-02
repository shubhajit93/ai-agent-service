package org.hishab.agent.cmd.api.dto.stt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hishab.agent.core.enums.SttStatus;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttRequest {
    private String name;
    private SttStatus status;
    private List<String> models;
}
