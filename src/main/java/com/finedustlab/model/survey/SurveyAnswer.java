package com.finedustlab.model.survey;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema
public class SurveyAnswer {
    @Schema(description = "개별 설문조사 아이디", defaultValue = "103")
    private int question_id;
    @Schema(type ="list", description = "설문조사 답변")
    private List<SurveySubQuestion> answers;
    @Schema(description = "설문 일자", defaultValue = "20240322")
    private String date;
}
