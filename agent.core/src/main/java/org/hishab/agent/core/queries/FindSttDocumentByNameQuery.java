package org.hishab.agent.core.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindSttDocumentByNameQuery {
    private String name;
} 