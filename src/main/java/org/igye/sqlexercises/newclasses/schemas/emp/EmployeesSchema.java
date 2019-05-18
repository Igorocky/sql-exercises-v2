package org.igye.sqlexercises.newclasses.schemas.emp;

import org.igye.sqlexercises.newclasses.Schema;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeesSchema extends Schema {

    @Override
    public String getId() {
        return "employees";
    }

    @Override
    public List<String> generateTestData() {
        List<String> res = new ArrayList<>();
        res.add("insert into Employee(ID, FIRST_NAME, LAST_NAME, SALARY) values (1, 'A', 'B', 545)");
        res.add("insert into Employee(ID, FIRST_NAME, LAST_NAME, SALARY) values (2, 'C', 'D', 788)");
        return res;
    }
}
