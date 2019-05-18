package org.igye.sqlexercises.newclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
public class ResultSetDto {
    private List<String> colNames;
    private List<Map<String, Object>> data;
}
