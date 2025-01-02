package org.hishab.agent.query.api.queries;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindTtsDocumentByIdQuery {
    private String id;
} 