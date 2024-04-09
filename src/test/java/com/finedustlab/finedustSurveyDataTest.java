package com.finedustlab;

import com.finedustlab.domain.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class finedustSurveyDataTest {
    @Autowired
    SurveyRepository surveyRepository;

    @Test
    public void getAnswerData(){
        Map<String,Map<String, Object>> allAnswerData = surveyRepository.findAnswerDataByUserType("elementary");
        System.out.println("allAnswerData = " + allAnswerData);
    }
}
