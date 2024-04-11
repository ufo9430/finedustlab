package com.finedustlab.model.survey;

import com.finedustlab.model.user.UserProfile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SurveyInputWrapper {
    private UserProfile user;
    private SurveyAnswer survey_data;
}
