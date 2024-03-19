package com.finedustlab.service;

import com.finedustlab.domain.repository.SurveyRepository;
import com.finedustlab.model.survey.SurveyInputWrapper;
import com.finedustlab.model.user.UserProfile;
import com.finedustlab.model.survey.SurveyAnswer;
import org.springframework.stereotype.Service;

@Service
public class SurveyService {
    private SurveyRepository surveyRepository = new SurveyRepository();


    public Object get(String userType) {
        return surveyRepository.findDataByID(userType);
    }

    public String set(SurveyInputWrapper data) {
        UserProfile profile = data.getUser();
        SurveyAnswer answer = data.getAnswer();

        String document_id = profile.getSchool_code()+"-"+
                profile.getGrade()+"-"+
                profile.getClass_num()+"-"+
                profile.getName()+"-"+
                answer.getDate();
        return surveyRepository.save(document_id,answer);
    }
}
