package org.igye.sqlexercises.newclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateQueryResponse {
    @Builder.Default
    private String status = "ok";
    private ResultSetDto actualResultSet;
    private Boolean passed;
    private String error;
}
