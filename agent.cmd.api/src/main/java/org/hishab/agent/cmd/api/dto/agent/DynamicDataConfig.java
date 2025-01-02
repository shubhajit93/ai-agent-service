package org.hishab.agent.cmd.api.dto.agent;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DynamicDataConfig {
    private String url;
    private String method;
    private int timeout;
    private Map<String, String> headers;
    private Map<String, String> body;
    private Map<String, String> query;
    private boolean cache;

    @Schema(description = "Response data configuration")
    private List<ResponseData> responseData;

}
