package com.finedustlab.model.survey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SurveyAnswer {
    private String type;
    private int answer_id;
    private String answer;
    private String sub_answer;
    private String date;
}
