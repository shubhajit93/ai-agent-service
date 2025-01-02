package org.hishab.agent.query.api.model.stt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hishab.agent.core.enums.SttStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "stt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SttDocument implements Serializable {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;  // Unique constraint on the provider field

    private SttStatus status;  // ACTIVE or DELETED

    private List<String> models = new ArrayList<>();
    private Instant createdAt;
    private Instant modifiedAt;
}
