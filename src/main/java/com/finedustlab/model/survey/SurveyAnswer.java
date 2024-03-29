package com.finedustlab.model.survey;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema
public class SurveyAnswer {
    @Schema(description = "설문조사 타입", allowableValues =
            {"multi_choice","ox","choice","number_picker","checkbox","text"})
    private String type;
    @Schema(description = "개별 설문조사 아이디", defaultValue = "103")
    private int answer_id;
    @Schema(description = "설문조사 답변", defaultValue = "답변")
    private String answer;
    @Schema(description = "설문조사 보조 답변", defaultValue = "보조 답변")
    private String sub_answer;
    @Schema(description = "설문 일자", defaultValue = "20240322")
    private String date;
}
