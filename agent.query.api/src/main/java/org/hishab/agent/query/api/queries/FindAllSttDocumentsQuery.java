package org.hishab.agent.query.api.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hishab.agent.core.enums.SttStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllSttDocumentsQuery {
    private String sortDirection;
    private SttStatus status;
    private int pageSize;
    private String pageToken;
}