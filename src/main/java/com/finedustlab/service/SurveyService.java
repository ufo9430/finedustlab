package com.finedustlab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.domain.repository.SurveyRepository;
import com.finedustlab.model.survey.SurveyInputWrapper;
import com.finedustlab.model.survey.SurveySubQuestion;
import com.finedustlab.model.user.StudentProfile;
import com.finedustlab.model.survey.SurveyAnswer;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class SurveyService {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private UserService userService;
    ObjectMapper objectMapper = new ObjectMapper();


    public Object get(String userType) {
        return surveyRepository.findDataByID(userType);
    }

    public String set(SurveyInputWrapper data) throws ExecutionException, InterruptedException{
        StudentProfile profile = data.getUser();
        SurveyAnswer answer = data.getSurvey_data();
        String teacherName = userService.getTeacherName(
                String.valueOf(profile.getSchool_code()),
                String.valueOf(profile.getGrade()),
                String.valueOf(profile.getClass_num()));
        return surveyRepository.save(profile, answer, teacherName);
    }



    @SuppressWarnings("unchecked")
    public void exportDataByAll(HttpServletResponse response) {
        try{
            String fileName = "설문결과-전체";
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            CellStyle style = workbook.createCellStyle();

            HashMap<String,String> types = new HashMap<>();
            types.put("초등","elementary");
            types.put("중등","middle");
            types.put("고등","high");
            types.put("교직원","teacher");

            for (String type : types.keySet()) {
                SXSSFSheet worksheet = workbook.createSheet(type);
                HashMap<String, Object> surveyData = objectMapper.convertValue(surveyRepository.findDataByID(types.get(type)), HashMap.class);
                Map<String, Map<String, Object>> answerData = surveyRepository.findAnswerDataByUserType(types.get(type));
                List<Map<String, Object>> surveyDataList = objectMapper.convertValue(surveyData.get("data"), List.class);

                makeSheet(worksheet, surveyDataList, answerData, style);
            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8")+".xlsx");

            workbook.write(response.getOutputStream());
            workbook.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void exportDataBySchoolInfo(HttpServletResponse response, String schoolCode, String grade, String class_num) {
        try{
            String fileName = "설문결과-전체";
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            CellStyle style = workbook.createCellStyle();

            HashMap<String,String> types = new HashMap<>();
            types.put("elementary","학생");
            types.put("middle","학생");
            types.put("high","학생");
            types.put("teacher","교직원");

            for (String type : types.keySet()) {
                HashMap<String, Object> surveyData = objectMapper.convertValue(surveyRepository.findDataByID(type), HashMap.class);
                Map<String, Map<String, Object>> answerData = surveyRepository.findAnswerDataBySchoolInfo(schoolCode,grade,class_num,type);
                if(answerData.isEmpty()) continue;
                List<Map<String, Object>> surveyDataList = objectMapper.convertValue(surveyData.get("data"), List.class);

                SXSSFSheet worksheet = workbook.createSheet(types.get(type));
                makeSheet(worksheet, surveyDataList, answerData, style);
            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8")+".xlsx");

            workbook.write(response.getOutputStream());
            workbook.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void makeSheet(SXSSFSheet worksheet, List<Map<String, Object>> surveyDataList, Map<String, Map<String, Object>> answerData, CellStyle style) {
        int rownum = 0;
        int count = 0;


        Row row = worksheet.createRow(rownum++);
        row.createCell(0).setCellValue("기본정보");
        row.createCell(8).setCellValue("설문응답");

        // 설문조사 아이디 작성
        row = worksheet.createRow(rownum++);
        row.createCell(0).setCellValue("담당선생님");
        row.createCell(1).setCellValue("이름");
        row.createCell(2).setCellValue("학교");
        row.createCell(3).setCellValue("학년");
        row.createCell(4).setCellValue("반");
        row.createCell(5).setCellValue("번호");
        row.createCell(6).setCellValue("설문응답 날짜");
        row.createCell(7).setCellValue("설문응답 시간");
        for (Map<String, Object> data : surveyDataList) {
            List<Object> subQuestions = objectMapper.convertValue(data.get("sub_questions"), List.class);
            String questionId = objectMapper.convertValue(data.get("id"), String.class);
            if(subQuestions.size() == 1){
                Cell cell = row.createCell((count)+8);
                cell.setCellValue("문항"+questionId);
                count++;
            }else{
                for (Object subQuestion : subQuestions) {
                    Cell cell = row.createCell((count)+8);
                    Map<String, Object> questionMap = objectMapper.convertValue(subQuestion, Map.class);
                    Integer subQuestionId = objectMapper.convertValue(questionMap.get("sub_question_id"), Integer.class);
                    cell.setCellValue("문항"+questionId+"-"+subQuestionId);
                    count++;
                }
            }
        }
        // 이용자 설문조사 쿼리 작성
        for (String s : answerData.keySet()) {
            // 아이디 = 학교코드 학년 반 담당선생님 번호 이름 게시일
            // 셀 = 담당선생님 이름 학교 학년 반 번호
            System.out.println("s = " + s);
            String[] answerParams = s.split("-");
            Map<String, Object> userAnswerMap = answerData.get(s);
            row = worksheet.createRow(rownum++);
            row.createCell(0).setCellValue(answerParams[3]);
            row.createCell(1).setCellValue(answerParams[5]);
            row.createCell(2).setCellValue(answerParams[0]);
            row.createCell(3).setCellValue(answerParams[1]);
            row.createCell(4).setCellValue(answerParams[2]);
            row.createCell(5).setCellValue(answerParams[4]);
            row.createCell(6).setCellValue(answerParams[6]);
            row.createCell(7).setCellValue(objectMapper.convertValue(
                    userAnswerMap.get("time"),String.class
            ));

            count = 8;
            for (Map<String, Object> data : surveyDataList) {
                List<Object> subQuestions = objectMapper.convertValue(data.get("sub_questions"), List.class);
                String questionId = objectMapper.convertValue(data.get("id"), String.class);
                Map<String, Object> subAnswers_obj = objectMapper.convertValue(
                        userAnswerMap.get(String.valueOf(questionId)),
                        Map.class);
                if(subAnswers_obj == null){
                    count++;
                    continue;
                }
                List<SurveySubQuestion> subAnswers = objectMapper.convertValue(subAnswers_obj.get("answer"), List.class);

                for (Object subQuestion : subQuestions) {
                    Cell cell = row.createCell(count);
                    Map<String, Object> questionMap = objectMapper.convertValue(subQuestion, Map.class);
                    Integer subQuestionId = objectMapper.convertValue(questionMap.get("sub_question_id"), Integer.class);
                    for (Object answerObj : subAnswers) {
                        SurveySubQuestion answer = objectMapper.convertValue(answerObj, SurveySubQuestion.class);
                        if(answer.getSub_question_id() == subQuestionId) {
                            String answerStr = answer.getSub_question_answer();
                            if(answer.getSub_question_input() != "") answerStr = answerStr + ", " + answer.getSub_question_input();
                            cell.setCellValue(answerStr);
                        }
                    }
                    count++;
                }
            }
            // 스타일 작성
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);

            worksheet.addMergedRegion(new CellRangeAddress(0,5,0,0));
            worksheet.addMergedRegion(new CellRangeAddress(6,count,0,0));
        }
    }
}
