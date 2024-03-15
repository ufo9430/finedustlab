package com.finedustlab.service;

import com.finedustlab.domain.repository.SurveyRepository;
import com.finedustlab.model.Survey;
import com.google.api.client.json.Json;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Service
public class SurveyService {
    private SurveyRepository surveyRepository = new SurveyRepository();


    public Response get(String userType) {
        String id = "";
        switch (userType){
            case "Elementary":
                id = "4qd0RfnxpG3Z91jnGSwY";
            case "High":
                id = "IxygMpwl8BMaT06s1yxq";
            case "Teacher":
                id = "fao43kLasUbodij1d1VN";
        }
        return surveyRepository.findByID(id);
    }

    public void set() {
        Survey survey1 = new Survey("number_picker","3","asdf");
        Survey survey2 = new Survey("ox", "예");
        Survey survey3 = new Survey("text", "텍스트");
        HashMap<String, Survey> surveyHashMap = new HashMap<>();
        surveyHashMap.put("1",survey1);
        surveyHashMap.put("2",survey2);
        surveyHashMap.put("3",survey3);
        surveyRepository.save(surveyHashMap);
    }
}
