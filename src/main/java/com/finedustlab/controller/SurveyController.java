package com.finedustlab.controller;

import com.finedustlab.model.Survey;
import com.finedustlab.service.SurveyService;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class SurveyController {
    private final SurveyService surveyService = new SurveyService();

    @GetMapping("/survey/get")
    public Response getSurveyData(@RequestParam("type") String type){
        Response response = surveyService.get(type);

        return response;
    }

    @GetMapping("/survey/set")
    public void setSurveyAnswer(){
        surveyService.set();
    }

}
