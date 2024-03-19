package com.finedustlab.service;

import com.finedustlab.domain.repository.SurveyRepository;
import com.finedustlab.model.UserProfileDto;
import com.finedustlab.model.SurveyAnswerDto;
import org.springframework.stereotype.Service;

@Service
public class SurveyService {
    private SurveyRepository surveyRepository = new SurveyRepository();


    public Object get(String userType) {
        return surveyRepository.findDataByID(userType);
    }

    public String set(UserProfileDto profile, SurveyAnswerDto answer) {
        String document_id = profile.getSchool_code()+"-"+
                profile.getGrade()+"-"+
                profile.getClass_num()+"-"+
                profile.getName();
        return surveyRepository.save(document_id,answer);
    }
}
