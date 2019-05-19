package org.igye.sqlexercises.newclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Exercise {
    private boolean ignore;
    private String id;
    private String title;
    private String description;
    private String schemaId;
    private String schemaDdl;
    private String dataGeneratorId;
    private List<String> testData;
    private ResultSetDto expectedResultSet;
    private String answer;
}
