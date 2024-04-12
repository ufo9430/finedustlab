package com.finedustlab.model.survey;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SurveySubQuestion {
    private int sub_question_id;
    @Schema(description = "설문조사 타입", allowableValues =
            {"multi_choice","ox","choice","number_picker","checkbox","text"})
    private String type;
    @Schema(description = "설문조사 답변", defaultValue ="답변")
    private String sub_question_answer;
}
