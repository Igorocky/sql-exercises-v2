package org.igye.sqlexercises.newclasses;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QueryExecutor {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public synchronized Pair<ResultSetDto, ResultSetDto> executeQueriesOnExampleData(
            String schemaId, List<String> data, String expectedQuery, String actualQuery) throws IOException, SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            if (schemaId != null) {
                ScriptUtils.executeSqlScript(
                        connection,
                        new ClassPathResource(getDdlPath(schemaId))
                );
            }
            for (String insert : data) {
                jdbcTemplate.execute(insert);
            }

            ResultSetDto actualResult = actualQuery != null ? executeQuery(actualQuery) : null;
            return Pair.of(executeQuery(expectedQuery), actualResult);
        }
    }

    @Transactional
    public ResultSetDto executeQuery(String query) {
        return executeQuery(query, new Object[]{});
    }

    @Transactional
    public ResultSetDto executeQuery(String query, Object[] params) {
        ResultSetDto res = new ResultSetDto();
        res.setColNames(new ArrayList<>());
        jdbcTemplate.query(query, params, (RowMapper<Void>) (resultSet, i) -> {
            if (res.getColNames().isEmpty()) {
                int colCnt = resultSet.getMetaData().getColumnCount();
                for (int c = 1; c <= colCnt; c++) {
                    res.getColNames().add(resultSet.getMetaData().getColumnLabel(c));
                }
            }
            return null;
        });
        res.setData(jdbcTemplate.queryForList(query, params));
        formatData(res.getData());
        return res;
    }

    private void formatData(List<Map<String, Object>> data) {
        for (Map<String, Object> row : data) {
            for (String col : row.keySet()) {
                Object val = row.get(col);
                if (val == null) {
                    row.put(col,"NULL");
                } else if (val instanceof Boolean) {
                    row.put(col,val.toString().toUpperCase());
                } else if (val instanceof Date || val instanceof Timestamp || val instanceof Boolean) {
                    row.put(col,val.toString());
                }
            }
        }
    }

    public String getDdlPath(String schemaId) {
        return "schemas/" + schemaId + ".sql";
    }
}
