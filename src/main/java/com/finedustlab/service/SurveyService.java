package com.finedustlab.service;

import com.finedustlab.domain.repository.SurveyRepository;
import com.finedustlab.model.Survey;
import com.google.api.client.json.Json;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class SurveyService {
    private SurveyRepository surveyRepository = new SurveyRepository();


    public Object get(String userType) {
        return surveyRepository.findDataByID(userType);
    }

    public void set(Map<String, String> params) {
        params.put("id","");
        surveyRepository.save(params);
    }
}
