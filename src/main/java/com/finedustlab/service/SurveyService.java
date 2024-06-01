package com.finedustlab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.domain.repository.SurveyRepository;
import com.finedustlab.model.survey.SurveyInputWrapper;
import com.finedustlab.model.survey.SurveySubQuestion;
import com.finedustlab.model.user.StudentProfile;
import com.finedustlab.model.survey.SurveyAnswer;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SurveyService {
    private SurveyRepository surveyRepository = new SurveyRepository();
    ObjectMapper objectMapper = new ObjectMapper();


    public Object get(String userType) {
        return surveyRepository.findDataByID(userType);
    }

    public String set(SurveyInputWrapper data) {
        StudentProfile profile = data.getUser();
        SurveyAnswer answer = data.getSurvey_data();
        return surveyRepository.save(profile, answer);
    }



    @SuppressWarnings("unchecked")
    public void exportDataToXls(HttpServletResponse response, String userType) {
        try{
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            SXSSFSheet worksheet = workbook.createSheet("설문 결과");
            int rownum = 0;

            String fileName = "설문결과-"+userType;

            HashMap<String, Object> map = objectMapper.convertValue(surveyRepository.findDataByID(userType), HashMap.class);
            Map<String, Map<String, Object>> answerData = surveyRepository.findAnswerDataByUserType(userType);

            List<Map<String, Object>> list = objectMapper.convertValue(map.get("data"), List.class);

            // 설문조사 아이디 작성
            Row row = worksheet.createRow(rownum++);
            row.createCell(0).setCellValue("번호");
            for(int i=0;i<list.size();i++){
                Cell cell = row.createCell(i+1);
                cell.setCellValue(objectMapper.convertValue(list.get(i).get("id"),String.class));
            }
            // 설문조사 물음 작성
            row = worksheet.createRow(rownum++);
            row.createCell(0).setCellValue("설문 내용");
            for(int i=0;i<list.size();i++){
                Cell cell = row.createCell(i+1);
                cell.setCellValue((String) list.get(i).get("question"));
            }
            // 이용자 설문조사 쿼리 작성
            int count = 0;
            for (String s : answerData.keySet()) {
                Map<String, Object> userAnswerMap = answerData.get(s);
                row = worksheet.createRow(rownum++);
                row.createCell(0).setCellValue(s);
                Row rowChecker = worksheet.getRow(0);
                count++;
                if(rowChecker == null) continue;
                System.out.println("count = " + count);
                for(int i=0;i<list.size();i++){
                    String questionId = rowChecker.getCell(i).getStringCellValue();
                    Map answerMap = objectMapper.convertValue(userAnswerMap.get(questionId), Map.class);
                    if(answerMap!=null){
                        List<SurveySubQuestion> answerList = (List<SurveySubQuestion>) answerMap.get("answer");
                        String answerStr = "";
                        for (Object o : answerList) {
                            SurveySubQuestion answer = objectMapper.convertValue(o, SurveySubQuestion.class);
                            answerStr = answerStr + "," + answer.getSub_question_answer();
                        }
                        if(!answerStr.isEmpty()){
                            answerStr = answerStr.substring(1);
                        }
                        row.createCell(i).setCellValue(answerStr);
                    }
                }
            }
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8")+".xlsx");

            workbook.write(response.getOutputStream());
            workbook.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
