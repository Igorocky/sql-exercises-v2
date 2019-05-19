package org.igye.sqlexercises.database;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryRecord {
    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();
    @Builder.Default
    private Instant dateTime = Instant.now();
    private String exerciseId;
    private String actualQuery;
    private boolean passed;
    private boolean wasError;
    private boolean reset;
}
