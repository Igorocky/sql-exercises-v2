package org.igye.sqlexercises.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.igye.sqlexercises.newclasses.ExampleData;
import org.igye.sqlexercises.newclasses.ExerciseFullDescription;
import org.igye.sqlexercises.newclasses.ExerciseFullDescriptionDto;
import org.igye.sqlexercises.newclasses.ExerciseShortDescriptionDto;
import org.igye.sqlexercises.newclasses.Schema;
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
    private List<Schema> schemas;

    private ObjectMapper mapper = new ObjectMapper();

    private List<ExerciseFullDescription> exercises;
    private List<ExerciseShortDescriptionDto> exercisesShortDescriptions;

    @PostConstruct
    public void init() throws IOException {
        exercises = loadExercises();
        exercisesShortDescriptions = exercises.stream()
                .map(e -> ExerciseShortDescriptionDto.builder().id(e.getId()).title(e.getTitle()).build())
                .collect(Collectors.toList());

        for (ExerciseFullDescription exercise : exercises) {
            exercise.setSchema(getSchema(exercise.getSchemaId()));
        }
    }

    @GetMapping("exercises")
    public String exercises(Model model) throws IOException {
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
                "exercise", loadFullDescriptionDto(id)
        ));
        return "index";
    }

    private ExerciseFullDescriptionDto loadFullDescriptionDto(String exerciseId) throws IOException, SQLException {
        ExerciseFullDescription fullDescription =
                exercises.stream().filter(e -> e.getId().equals(exerciseId)).findFirst().get();
        if (fullDescription.getDescription() == null) {
            String exerciseDir = EXERCISES_DIR + "/" + fullDescription.getId();
            fullDescription.setDescription(readFileToString(exerciseDir + "/description.txt"));
            fullDescription.setAnswer(readFileToString(exerciseDir + "/ans.sql"));
            ExampleData exampleData = fullDescription.getSchema().executeQueryOnExampleData(fullDescription.getAnswer());
            fullDescription.setExampleOutput(exampleData.getQueryResult());
        }
        return ExerciseFullDescriptionDto.builder()
                .id(fullDescription.getId())
                .title(fullDescription.getTitle())
                .description(fullDescription.getDescription())
                .exampleOutput(fullDescription.getExampleOutput())
                .build();
    }

    private List<ExerciseFullDescription> loadExercises() throws IOException {
        return mapper.readValue(
                readFileToString(EXERCISES_CONFIG_JSON),
                new TypeReference<List<ExerciseFullDescription>>(){}
        );
    }

    private Schema getSchema(String schemaId) {
        return schemas.stream().filter(s->s.getId().equals(schemaId)).findFirst().get();
    }

}
