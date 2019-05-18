package org.igye.sqlexercises.newclasses.schemas.emp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class Employee {
    private Integer id;
    private String firstName;
    private String lastName;
    private BigDecimal salary;
}
