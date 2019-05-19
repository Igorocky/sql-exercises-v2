package org.igye.sqlexercises.newclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ExerciseFullDescriptionDto {
    private String id;
    private String title;
    private boolean completed;
    private String description;
    private ResultSetDto expectedResultSet;
    private String schemaDdl;
}
