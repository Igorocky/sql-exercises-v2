package org.igye.sqlexercises.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.igye.sqlexercises.newclasses.ExampleData;
import org.igye.sqlexercises.newclasses.Exercise;
import org.igye.sqlexercises.newclasses.ExerciseFullDescriptionDto;
import org.igye.sqlexercises.newclasses.ExerciseShortDescriptionDto;
import org.igye.sqlexercises.newclasses.QueryExecutor;
import org.igye.sqlexercises.newclasses.TestDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.igye.sqlexercises.common.OutlineUtils.createResponse;
import static org.igye.sqlexercises.common.OutlineUtils.map;
import static org.igye.sqlexercises.common.OutlineUtils.mapF;
import static org.igye.sqlexercises.common.OutlineUtils.readFileToString;
import static org.igye.sqlexercises.common.OutlineUtils.redirect;

@Controller
@RequestMapping(NodeController.PREFIX)
public class NodeController {
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

    private ExerciseFullDescriptionDto getFullDescriptionDto(String exerciseId) throws IOException, SQLException {
        Exercise fullDescription =
                exercises.stream().filter(e -> e.getId().equals(exerciseId)).findFirst().get();
        if (fullDescription.getExpectedResultSet() == null) {
            String exerciseDir = EXERCISES_DIR + "/" + fullDescription.getId();
            fullDescription.setDescription(readFileToString(exerciseDir + "/description.txt"));
            fullDescription.setSchemaDdl(readFileToString(queryExecutor.getDdlPath(fullDescription.getSchemaId())));
            fullDescription.setTestData(
                    testDataGenerators.stream()
                            .filter(g->g.getId().equals(fullDescription.getDataGeneratorId()))
                            .findFirst().get()
                            .generateTestData()
            );
            fullDescription.setAnswer(readFileToString(exerciseDir + "/ans.sql"));
            fullDescription.setExpectedResultSet(queryExecutor.executeQueriesOnExampleData(
                    fullDescription.getSchemaId(),
                    fullDescription.getTestData(),
                    fullDescription.getAnswer(),
                    null
            ).getLeft());
        }
        return ExerciseFullDescriptionDto.builder()
                .id(fullDescription.getId())
                .title(fullDescription.getTitle())
                .description(fullDescription.getDescription())
                .expectedResultSet(fullDescription.getExpectedResultSet())
                .schemaDdl(fullDescription.getSchemaDdl())
                .build();
    }

    private List<Exercise> loadExercises() throws IOException {
        return mapper.readValue(
                readFileToString(EXERCISES_CONFIG_JSON),
                new TypeReference<List<Exercise>>(){}
        );
    }
}
