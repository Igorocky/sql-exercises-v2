package org.igye.sqlexercises.newclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExampleData {
    private String ddl;
    private List<String> testData;
    private List<Map<String, Object>> queryResult;
}
