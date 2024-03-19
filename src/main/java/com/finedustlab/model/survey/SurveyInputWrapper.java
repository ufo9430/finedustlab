package com.finedustlab.model.survey;

import com.finedustlab.model.user.UserProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SurveyInputWrapper {
    private UserProfile user;
    private SurveyAnswer answer;
}
