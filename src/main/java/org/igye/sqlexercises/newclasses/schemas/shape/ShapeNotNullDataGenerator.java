package org.igye.sqlexercises.newclasses.schemas.shape;

import org.igye.sqlexercises.newclasses.TestDataGenerator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.igye.sqlexercises.common.ExercisesUtils.readLines;

@Service
public class ShapeNotNullDataGenerator extends TestDataGenerator {
    private final String RECTANGLE = "rectangle";
    private final String CIRCLE = "circle";
    private List<String> kindsOfShapes = Arrays.asList(RECTANGLE, CIRCLE);

    public ShapeNotNullDataGenerator() throws IOException {
    }

    @Override
    public String getId() {
        return "shapes-not-null";
    }

    @Override
    public List<String> generateTestData() throws Exception {
        if (1 == 1) {
            return readLines("schemas/shapes-not-null-data.sql");
        } else {
            List<String> res = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                String kind = randomElem(kindsOfShapes);
                Shape.ShapeBuilder shape = Shape.builder().id(nextId()).kind(kind);
                if (RECTANGLE.equals(kind)) {
                    shape.width(BigDecimal.valueOf(randomDouble(1,15)));
                    shape.height(BigDecimal.valueOf(randomDouble(1,15)));
                    shape.radius(BigDecimal.ZERO);
                } else if (CIRCLE.equals(kind)) {
                    shape.radius(BigDecimal.valueOf(randomDouble(1,15)));
                    shape.width(BigDecimal.ZERO);
                    shape.height(BigDecimal.ZERO);
                }
                res.add(toInsert(shape.build()));
            }
            return res;
        }
    }

    private String toInsert(Shape shape) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into Shape(ID, KIND, HEIGHT, WIDTH, RADIUS) values (");
        intAttr(sb, shape.getId());
        sb.append(", ");
        stringAttr(sb, shape.getKind());
        sb.append(", ");
        bigDecimalAttr(sb, shape.getHeight());
        sb.append(", ");
        bigDecimalAttr(sb, shape.getWidth());
        sb.append(", ");
        bigDecimalAttr(sb, shape.getRadius());
        sb.append(")");
        return sb.toString();
    }
}
