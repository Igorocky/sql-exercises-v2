package org.igye.sqlexercises.newclasses;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
public class ResultSetDto {
    private List<String> colNames;
    private List<Map<String, Object>> data;
}
