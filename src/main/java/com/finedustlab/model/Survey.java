package com.finedustlab.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Survey {
    private String type;
    private String answer;
    private String sub_answer;

    public Survey(String type, String answer) {
        this.type = type;
        this.answer = answer;
    }
    public Survey(String type, String answer, String sub_answer) {
        this.type = type;
        this.answer = answer;
        this.sub_answer = sub_answer;
    }
}
