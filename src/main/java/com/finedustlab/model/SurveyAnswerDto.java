package com.finedustlab.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SurveyAnswerDto {
    private String type;
    private int answer_id;
    private String answer;
    private String sub_answer;

}
