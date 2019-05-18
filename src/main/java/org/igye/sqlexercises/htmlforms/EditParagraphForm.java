package org.igye.sqlexercises.htmlforms;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class EditParagraphForm extends EditNodeForm {
    private String name;
    private UUID iconId;
    private boolean sol;
}
