package org.igye.sqlexercises.newclasses.schemas.emp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Employee {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate worksSince;
    private Integer depId;
    private BigDecimal salary;
}
