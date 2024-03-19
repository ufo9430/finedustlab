package com.finedustlab.controller;

import com.finedustlab.model.survey.SurveyInputWrapper;
import com.finedustlab.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class SurveyController {
    private final SurveyService surveyService = new SurveyService();

    @Tag(name = "getSurveyData")
    @Operation(description = "Request parameter를 통해 이용자 별 데이터를 받습니다. elementary : 초등 high : 중/고등 teacher : 교직원")
    @GetMapping("/survey/data")
    @ResponseBody
    public Object getSurveyData(@RequestParam("type") String type){
        return surveyService.get(type);
    }

    @Tag(name = "setSurveyAnswer")
    @PostMapping("/survey/set")
    @ResponseBody
    public String setSurveyAnswer(@RequestBody SurveyInputWrapper input_data){
        return surveyService.set(input_data);
    }

}
