package org.igye.sqlexercises.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.igye.sqlexercises.newclasses.Exercise;
import org.igye.sqlexercises.newclasses.ExerciseFullDescriptionDto;
import org.igye.sqlexercises.newclasses.ExerciseShortDescriptionDto;
import org.igye.sqlexercises.newclasses.QueryExecutor;
import org.igye.sqlexercises.newclasses.ResultSetDto;
import org.igye.sqlexercises.newclasses.TestDataGenerator;
import org.igye.sqlexercises.newclasses.ValidateQueryRequest;
import org.igye.sqlexercises.newclasses.ValidateQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.igye.sqlexercises.common.ExercisesUtils.mapF;
import static org.igye.sqlexercises.common.ExercisesUtils.readString;

@Controller
@RequestMapping(ExerciseController.PREFIX)
public class ExerciseController {
    protected static final String PREFIX = "";
    public static final String EXERCISES_DIR = "exercises";
    public static final String EXERCISES_CONFIG_JSON = EXERCISES_DIR + "/config.json";

    @Autowired
    private QueryExecutor queryExecutor;

    private ObjectMapper mapper = new ObjectMapper();

    private List<Exercise> exercises;
    private List<ExerciseShortDescriptionDto> exercisesShortDescriptions;
    @Autowired
    private List<TestDataGenerator> testDataGenerators;

    @PostConstruct
    public void init() throws IOException {
        exercises = loadExercises();
        exercisesShortDescriptions = exercises.stream().map(ex ->
                ExerciseShortDescriptionDto.builder().id(ex.getId()).title(ex.getTitle()).build()
        ).collect(Collectors.toList());
    }

    @GetMapping("exercises")
    public String exercises(Model model) {
        model.addAttribute("pageType", "SqlExercisesList");
        model.addAttribute("pageData", mapF(
                "exercises", exercisesShortDescriptions
        ));
        return "index";
    }

    @GetMapping("exercise/{id}")
    public String exercise(@PathVariable String id, Model model) throws IOException, SQLException {
        model.addAttribute("pageType", "SqlExerciseFullDescription");
        model.addAttribute("pageData", mapF(
                "exercise", getFullDescriptionDto(id)
        ));
        return "index";
    }

    @PostMapping("exercise/{id}/validate")
    @ResponseBody
    public ValidateQueryResponse validate(@PathVariable String id,
                           @RequestBody ValidateQueryRequest request) throws IOException, SQLException {
        Exercise exercise = getExercise(id);
        if (request.getActualQuery() == null) {
            return ValidateQueryResponse.builder()
                    .passed(false)
                    .error("The query was not entered.")
                    .expectedResultSet(
                            queryExecutor.executeQueriesOnExampleData(
                                    exercise.getSchemaId(), exercise.getTestData(), exercise.getAnswer(), null
                            ).getLeft()
                    )
                    .build();
        }
        Pair<ResultSetDto, ResultSetDto> resultSets;
        try {
            resultSets = queryExecutor.executeQueriesOnExampleData(
                    exercise.getSchemaId(), exercise.getTestData(), exercise.getAnswer(), request.getActualQuery()
            );
        } catch (Exception ex) {
            return ValidateQueryResponse.builder()
                    .passed(false)
                    .error(ex.getMessage())
                    .expectedResultSet(
                            queryExecutor.executeQueriesOnExampleData(
                                    exercise.getSchemaId(), exercise.getTestData(), exercise.getAnswer(), null
                            ).getLeft()
                    )
                    .build();
        }
        return ValidateQueryResponse.builder()
                .expectedResultSet(resultSets.getLeft())
                .actualResultSet(resultSets.getRight())
                .passed(resultSets.getLeft().equals(resultSets.getRight()))
                .build();
    }

    @GetMapping("exercise/{id}/testdata")
    @ResponseBody
    public String getTestData(@PathVariable String id) throws IOException, SQLException {
        return testDataToString(getExercise(id).getTestData());
    }

    private Exercise getExercise(String exerciseId) throws IOException, SQLException {
        Exercise fullDescription =
                exercises.stream().filter(e -> e.getId().equals(exerciseId)).findFirst().get();
        if (fullDescription.getExpectedResultSet() == null) {
            String exerciseDir = EXERCISES_DIR + "/" + fullDescription.getId();
            fullDescription.setDescription(readString(exerciseDir + "/description.txt"));
            fullDescription.setSchemaDdl(readString(queryExecutor.getDdlPath(fullDescription.getSchemaId())));
            fullDescription.setTestData(
                    testDataGenerators.stream()
                            .filter(g->g.getId().equals(fullDescription.getDataGeneratorId()))
                            .findFirst().get()
                            .generateTestData()
            );
            fullDescription.setAnswer(readString(exerciseDir + "/ans.sql"));
            fullDescription.setExpectedResultSet(queryExecutor.executeQueriesOnExampleData(
                    fullDescription.getSchemaId(),
                    fullDescription.getTestData(),
                    fullDescription.getAnswer(),
                    null
            ).getLeft());
        }
        return fullDescription;
    }

    private ExerciseFullDescriptionDto getFullDescriptionDto(String exerciseId) throws IOException, SQLException {
        Exercise fullDescription = getExercise(exerciseId);
        return ExerciseFullDescriptionDto.builder()
                .id(fullDescription.getId())
                .title(fullDescription.getTitle())
                .description(fullDescription.getDescription())
                .expectedResultSet(fullDescription.getExpectedResultSet())
                .schemaDdl(fullDescription.getSchemaDdl())
                .build();
    }

    private String testDataToString(List<String> testData) {
        return StringUtils.join(testData,";\n") + ";";
    }

    private List<Exercise> loadExercises() throws IOException {
        return mapper.readValue(
                readString(EXERCISES_CONFIG_JSON),
                new TypeReference<List<Exercise>>(){}
        );
    }
}
