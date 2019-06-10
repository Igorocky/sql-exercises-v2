package org.igye.sqlexercises.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.igye.sqlexercises.database.HistoryRecord;
import org.igye.sqlexercises.database.HistoryRecordRepo;
import org.igye.sqlexercises.exceptions.ExerciseException;
import org.igye.sqlexercises.newclasses.Exercise;
import org.igye.sqlexercises.newclasses.ExerciseFullDescriptionDto;
import org.igye.sqlexercises.newclasses.ExerciseShortDescriptionDto;
import org.igye.sqlexercises.newclasses.QueryExecutor;
import org.igye.sqlexercises.newclasses.ResultSetDto;
import org.igye.sqlexercises.newclasses.TestDataGenerator;
import org.igye.sqlexercises.newclasses.ValidateQueryRequest;
import org.igye.sqlexercises.newclasses.ValidateQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.igye.sqlexercises.common.ExercisesUtils.*;

@Controller
@RequestMapping(ExerciseController.PREFIX)
public class ExerciseController {
    protected static final String PREFIX = "";
    public static final String EXERCISES_DIR = "exercises";
    public static final String EXERCISES_CONFIG_JSON = EXERCISES_DIR + "/config.json";

    @Value("${h2.version}")
    private String h2Version;
    @Value("${backup.dir}")
    private String backupDirPath;
    @Value("${app.adminMode}")
    private boolean adminMode;

    @Autowired
    private QueryExecutor queryExecutor;
    @Autowired
    private HistoryRecordRepo historyRecordRepo;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    private List<Exercise> exercises;
    @Autowired
    private List<TestDataGenerator> testDataGenerators;

    @PostConstruct
    public void init() {
        exercises = loadExercises(
                "exercises/exercises1.sql"
                ,"exercises/exercises2.sql"
                ,"exercises/exercises3.sql"
//                ,"exercises/exercises4-case-when-emp.sql"
                ,"exercises/exercises5-grouping.sql"
                , "exercises/exercises6-expressions.sql"
        );
    }

    @PreDestroy
    public void preDestroy() throws SQLException {
        backup(backupDirPath, jdbcTemplate.getDataSource().getConnection(), h2Version);
    }

    @GetMapping("exercises")
    public String exercises(Model model) {
        model.addAttribute("pageType", "SqlExercisesList");
        List<ExerciseShortDescriptionDto> exercisesShortDescriptions = getExercisesShortDescriptions();
        exercisesShortDescriptions.stream().map(ex -> {
            Exercise full = getExercise(ex.getId());
            return "/*====================================\n"
                    + "id=" + full.getId() + " schema=" + full.getSchemaId() + " gen=" + full.getDataGeneratorId() + "\n"
                    + "\n"
                    + full.getTitle() + "\n"
                    + "\n"
                    + full.getDescription() + "\n"
                    + "*/----------------------------------\n"
                    + full.getAnswer() + "\n";
        }).collect(Collectors.toList()).forEach(System.out::println);
        model.addAttribute("pageData", mapF(
                "exercises", exercisesShortDescriptions
        ));
        return "index";
    }

    private List<Exercise> loadExercises(String... paths) {
        List<Exercise> res = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        int state = 1;
        for (String path : paths) {
            for (String line : readLines(path)) {
                Exercise exercise = res.size() > 0 ? res.get(res.size() - 1) : null;
                if (line.startsWith("/*==========")) {
                    res.add(new Exercise());
                    state = 1;
                } else if (line.startsWith("*/----------")) {
                    state = 4;
                    exercise.setAnswer("");
                } else if (state == 1) {
                    String[] parts = line.split("[\\s=]");
                    String id = parts[1];
                    if (ids.contains(id)) {
                        throw new ExerciseException("Found duplicated exercise with id '" + id + "'");
                    } else {
                        ids.add(id);
                    }
                    exercise.setId(id);
                    exercise.setSchemaId(parts[3]);
                    exercise.setDataGeneratorId(parts[5]);
                    state = 2;
                } else if (state == 2) {
                    if (StringUtils.isNoneBlank(line)) {
                        exercise.setTitle(line);
                        exercise.setDescription("");
                        state = 3;
                    }
                } else if (state == 3) {
                    if (StringUtils.isNoneBlank(line)) {
                        exercise.setDescription(exercise.getDescription() + line + "\n");
                    }
                } else if (state == 4) {
                    exercise.setAnswer(exercise.getAnswer() + line + "\n");
                }
            }
        }
        return res;
    }

    @GetMapping("exercise/{id}")
    public String exercise(@PathVariable String id, Model model) throws Exception {
        model.addAttribute("pageType", "SqlExerciseFullDescription");
        model.addAttribute("pageData", mapF(
                "exercise", getFullDescriptionDto(id)
        ));
        return "index";
    }

    @PostMapping("exercise/{id}/validate")
    @ResponseBody
    public ValidateQueryResponse validate(@PathVariable String id,
                           @RequestBody ValidateQueryRequest request) throws Exception {
        Exercise exercise = getExercise(id);
        ValidateQueryResponse response;
        if (request.getActualQuery() == null) {
            response = ValidateQueryResponse.builder()
                    .passed(false)
                    .error("The query was not entered.")
                    .expectedResultSet(
                            queryExecutor.executeQueriesOnExampleData(
                                    exercise.getSchemaId(), exercise.getTestData(), exercise.getAnswer(), null
                            ).getLeft()
                    )
                    .build();
        } else {
            Pair<ResultSetDto, ResultSetDto> resultSets;
            try {
                resultSets = queryExecutor.executeQueriesOnExampleData(
                        exercise.getSchemaId(), exercise.getTestData(), exercise.getAnswer(), request.getActualQuery()
                );
                response = ValidateQueryResponse.builder()
                        .expectedResultSet(resultSets.getLeft())
                        .actualResultSet(resultSets.getRight())
                        .passed(resultSets.getLeft().equals(resultSets.getRight()))
                        .build();
            } catch (Exception ex) {
                response = ValidateQueryResponse.builder()
                        .passed(false)
                        .error(ex.getMessage())
                        .expectedResultSet(
                                queryExecutor.executeQueriesOnExampleData(
                                        exercise.getSchemaId(), exercise.getTestData(), exercise.getAnswer(), null
                                ).getLeft()
                        )
                        .build();
            }
        }
        logResponse(id,request.getActualQuery(),response);
        return response;
    }

    @PostMapping("exercise/{id}/reset")
    @ResponseBody
    public void reset(@PathVariable String id) {
        historyRecordRepo.save(HistoryRecord.builder()
                .exerciseId(id)
                .reset(true)
                .build()
        );
    }

    private void logResponse(String id, String actualQuery, ValidateQueryResponse response) {
        historyRecordRepo.save(HistoryRecord.builder()
                .exerciseId(id)
                .actualQuery(actualQuery)
                .passed(response.getPassed())
                .wasError(response.getError()!=null)
                .build()
        );
    }

    @GetMapping("exercise/{id}/testdata")
    @ResponseBody
    public String getTestData(@PathVariable String id) {
        return testDataToString(getExercise(id).getTestData());
    }

    @GetMapping("exercise/{id}/history")
    @ResponseBody
    public ResultSetDto getHistory(@PathVariable String id) throws IOException, SQLException {
        return queryExecutor.executeQuery(
                "select DATE_TIME UTC, PASSED as P, WAS_ERROR E, RESET R, ACTUAL_QUERY" +
                        " from history_record where exercise_id = ? order by date_time",
                new Object[]{id}
        );
    }

    private Exercise getExercise(String exerciseId) {
        try {
            Exercise fullDescription =
                    exercises.stream().filter(e -> e.getId().equals(exerciseId)).findFirst().get();
            if (fullDescription.getExpectedResultSet() == null) {
                if (!"none".equals(fullDescription.getSchemaId())) {
                    fullDescription.setSchemaDdl(readString(queryExecutor.getDdlPath(fullDescription.getSchemaId())));
                } else {
                    fullDescription.setSchemaId(null);
                }
                if (!"none".equals(fullDescription.getDataGeneratorId())) {
                    fullDescription.setTestData(
                            testDataGenerators.stream()
                                    .filter(g->g.getId().equals(fullDescription.getDataGeneratorId()))
                                    .findFirst().get()
                                    .generateTestData()
                    );
                } else {
                    fullDescription.setTestData(Collections.emptyList());
                }
                fullDescription.setExpectedResultSet(queryExecutor.executeQueriesOnExampleData(
                        fullDescription.getSchemaId(),
                        fullDescription.getTestData(),
                        fullDescription.getAnswer(),
                        null
                ).getLeft());
            }
            return fullDescription;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ExerciseFullDescriptionDto getFullDescriptionDto(String exerciseId) {
        Exercise fullDescription = getExercise(exerciseId);
        return ExerciseFullDescriptionDto.builder()
                .isAdmin(adminMode)
                .id(fullDescription.getId())
                .title(fullDescription.getTitle())
                .completed(getCompleted(exerciseId))
                .description(fullDescription.getDescription())
                .expectedResultSet(fullDescription.getExpectedResultSet())
                .schemaDdl(fullDescription.getSchemaDdl())
                .build();
    }

    private String testDataToString(List<String> testData) {
        return StringUtils.join(testData,";\n") + ";";
    }

    private List<ExerciseShortDescriptionDto> getExercisesShortDescriptions() {
        return exercises.stream().map(ex -> ExerciseShortDescriptionDto.builder()
                .id(ex.getId())
                .title(ex.getTitle())
                .completed(getCompleted(ex.getId()))
                .build()
        ).collect(Collectors.toList());
    }

    private Boolean getCompleted(String exerciseId) {
        return historyRecordRepo.findByExerciseIdOrderByDateTimeDesc(exerciseId).stream()
                .filter(rec -> rec.isPassed() || rec.isReset())
                .findFirst()
        .map(HistoryRecord::isPassed)
        .orElse(false);
    }
}
