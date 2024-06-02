package com.finedustlab.controller;

import com.finedustlab.model.survey.SurveyInputWrapper;
import com.finedustlab.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;


@Controller
public class SurveyController {
    @Autowired
    private SurveyService surveyService;


    @Tag(name = "getSurveyData")
    @Operation(description = "Request parameter를 통해 이용자 별 데이터를 받습니다. elementary : 초등 high : 중/고등 teacher : 교직원")
    @GetMapping("/survey/data")
    @ResponseBody
    public Object getSurveyData(@RequestParam("type") String type){
        return surveyService.get(type);
    }

    @Tag(name = "setSurveyAnswer")
    @Operation(description = "개별 설문조사 문항에 따라 데이터를 저장합니다. profile에 따라 각 문항을 DB에 저장합니다.")
    @PostMapping("/survey/set")
    @ResponseBody
    public String setSurveyAnswer(@RequestBody SurveyInputWrapper input_data) throws ExecutionException, InterruptedException{
        return surveyService.set(input_data);
    }


    @Tag(name = "getSurveyAnswerByXls")
    @Operation(description = "이용자 학교 정보에 따라 설문조사 데이터를 xls 파일로 추출합니다. 파라미터 입력이 없을 경우 전체 데이터를 불러옵니다.")
    @GetMapping("/survey/download")
    @ResponseBody
    public void getSurveyAnswerByXls(HttpServletResponse response,
                                     @RequestParam(required = false) String schoolCode,
                                     @RequestParam(required = false) String grade,
                                     @RequestParam(required = false) String class_num){
        if(schoolCode == null && grade == null && class_num == null){
            surveyService.exportDataByAll(response);
        }else if(schoolCode != null && grade != null && class_num != null){
            surveyService.exportDataBySchoolInfo(response,schoolCode,grade,class_num);
        }
    }
}
