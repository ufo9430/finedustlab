package com.finedustlab.controller;

import com.finedustlab.service.SurveyService;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
public class SurveyController {
    private final SurveyService surveyService = new SurveyService();

    @GetMapping("/survey/data")
    @ResponseBody
    public Object getSurveyData(@RequestParam("type") String type){
        return surveyService.get(type);
    }

    @PostMapping("/survey/set")
    public void setSurveyAnswer(@RequestBody Map<String, String> params){

        surveyService.set(params);
    }

}
