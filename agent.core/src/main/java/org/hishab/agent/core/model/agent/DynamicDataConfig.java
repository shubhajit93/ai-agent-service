package org.hishab.agent.core.model.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
//@JsonSubTypes({ @JsonSubTypes.Type(value = ResponseData.class, name = "responseData")})
public class DynamicDataConfig implements Serializable {
    private String url;
    private String method;
    private int timeout;
    private Map<String, String> headers;
    private Map<String, String> body;
    private Map<String, String> query;
    private boolean cache;

    //    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    @JsonProperty("response_data")
    private List<ResponseData> responseData;

}
