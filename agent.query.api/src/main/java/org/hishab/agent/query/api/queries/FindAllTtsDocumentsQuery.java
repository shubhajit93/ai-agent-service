package org.hishab.agent.query.api.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hishab.agent.core.enums.TtsStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllTtsDocumentsQuery {
    private String sortDirection;
    private TtsStatus status;
    private int pageSize;
    private String pageToken;
} 