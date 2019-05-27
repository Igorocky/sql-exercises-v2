package org.igye.sqlexercises.newclasses.schemas.shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class Shape {
    private Integer id;
    private String kind;
    private BigDecimal height;
    private BigDecimal width;
    private BigDecimal radius;
}
