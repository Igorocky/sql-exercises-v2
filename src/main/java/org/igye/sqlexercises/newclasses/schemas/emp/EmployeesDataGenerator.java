package org.igye.sqlexercises.newclasses.schemas.emp;

import org.igye.sqlexercises.newclasses.TestDataGenerator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.igye.sqlexercises.common.ExercisesUtils.readLines;

@Service
public class EmployeesDataGenerator extends TestDataGenerator {
    public static final String RANDOM_DATA = "random-data";
    private List<String> firstNames;
    private List<String> lastNames;

    public EmployeesDataGenerator() throws IOException {
        firstNames = readLines(RANDOM_DATA + "/" + "female-first-names.txt");
        firstNames.addAll(readLines(RANDOM_DATA + "/" + "male-first-names.txt"));
        lastNames = readLines(RANDOM_DATA + "/" + "last-names.txt");
    }

    @Override
    public String getId() {
        return "employees";
    }

    @Override
    public List<String> generateTestData() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            LocalDate dateOfBirth = randomDate(LocalDate.now().minusYears(50), LocalDate.now().minusYears(20));
            res.add(toInsert(Employee.builder()
                    .id(nextId())
                    .firstName(randomElem(firstNames))
                    .lastName(randomElem(lastNames))
                    .dateOfBirth(dateOfBirth)
                    .worksSince(randomDate(dateOfBirth.plusYears(20), LocalDate.now()))
                    .depId(rnd.nextInt(5)+1)
                    .salary(BigDecimal.valueOf(randomDouble(1000,5000)))
                    .build())
            );
        }
        return res;
    }

    private String toInsert(Employee emp) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into Employee(ID, FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, WORKS_SINCE, SALARY, DEP_ID) values (");
        intAttr(sb, emp.getId());
        sb.append(", ");
        stringAttr(sb, emp.getFirstName());
        sb.append(", ");
        stringAttr(sb, emp.getLastName());
        sb.append(", ");
        dateAttr(sb, emp.getDateOfBirth());
        sb.append(", ");
        dateAttr(sb, emp.getWorksSince());
        sb.append(", ");
        bigDecimalAttr(sb, emp.getSalary());
        sb.append(", ");
        intAttr(sb, emp.getDepId());
        sb.append(")");
        return sb.toString();
    }
}
