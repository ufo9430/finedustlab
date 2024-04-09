package com.finedustlab.service;

import com.finedustlab.domain.repository.SurveyRepository;
import com.finedustlab.model.survey.SurveyInputWrapper;
import com.finedustlab.model.user.UserProfile;
import com.finedustlab.model.survey.SurveyAnswer;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SurveyService {
    private SurveyRepository surveyRepository = new SurveyRepository();


    public Object get(String userType) {
        return surveyRepository.findDataByID(userType);
    }

    public String set(SurveyInputWrapper data) {
        UserProfile profile = data.getUser();
        SurveyAnswer answer = data.getAnswer();

        String document_id =
                profile.getUser_type()+"-"+
                profile.getSchool_code()+"-"+
                profile.getGrade()+"-"+
                profile.getClass_num()+"-"+
                profile.getName()+"-"+
                profile.getStudent_num()+"-"+
                answer.getDate();
        return surveyRepository.save(document_id,answer);
    }



    public void exportDataToXls(){
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sxssfSheet = workbook.createSheet("설문 결과");

        Map<String, Map<String, Object>> answerData = surveyRepository.findAllAnswerData();
        for (String s : answerData.keySet()) {
            Map<String, Object> answer = answerData.get(s);

        }


    }

}
