package org.igye.sqlexercises.newclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ExerciseFullDescription {
    private String id;
    private String title;
    private String description;
    private List<Map<String, Object>> exampleOutput;
    private String schemaId;
    private String answer;
    private Schema schema;
}
