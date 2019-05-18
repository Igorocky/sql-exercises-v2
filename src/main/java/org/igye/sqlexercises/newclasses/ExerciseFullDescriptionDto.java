package org.igye.sqlexercises.newclasses;

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
public class ExerciseFullDescriptionDto {
    private String id;
    private String title;
    private String description;
    private ResultSetDto expectedResultSet;
    private String schemaDdl;
}
