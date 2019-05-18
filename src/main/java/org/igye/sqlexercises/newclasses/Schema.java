package org.igye.sqlexercises.newclasses;

import org.igye.sqlexercises.common.OutlineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public abstract class Schema {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public abstract String getId();
    public abstract List<String> generateTestData();

    @Transactional
    public synchronized ExampleData executeQueryOnExampleData(String query) throws IOException, SQLException {
        String ddlPath = "schemas/" + getId() + ".sql";
        String ddl = OutlineUtils.readFileToString(ddlPath);
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource(ddlPath));
        List<String> testData = generateTestData();
        for (String insert : testData) {
            jdbcTemplate.execute(insert);
        }
        return ExampleData.builder()
                .ddl(ddl)
                .testData(testData)
                .queryResult(jdbcTemplate.queryForList(query))
                .build();
    }
}
