package com.finedustlab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.domain.repository.SurveyRepository;
import com.finedustlab.model.survey.SurveyInputWrapper;
import com.finedustlab.model.survey.SurveySubQuestion;
import com.finedustlab.model.user.StudentProfile;
import com.finedustlab.model.survey.SurveyAnswer;
import com.finedustlab.model.user.UserProfile;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
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
//                worksheet.trackAllColumnsForAutoSizing();

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
//                worksheet.trackAllColumnsForAutoSizing();
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

        //스타일 생성
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        rownum = 1;
        Row row;
        Cell cell;

        // 설문조사 아이디 작성
        row = worksheet.createRow(rownum++);
        cell = row.createCell(0);
        cell.setCellValue("담당선생님");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("이름");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("학교");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("학년");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("반");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("번호");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("설문응답\n날짜");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("설문응답\n시간");
        cell.setCellStyle(style);

        for (Map<String, Object> data : surveyDataList) {
            List<Object> subQuestions = objectMapper.convertValue(data.get("sub_questions"), List.class);
            String questionId = objectMapper.convertValue(data.get("id"), String.class);
            if(subQuestions.size() == 1){
                cell = row.createCell((count)+8);
                cell.setCellValue("문항"+questionId);
                cell.setCellStyle(style);

                count++;
            }else{
                for (Object subQuestion : subQuestions) {
                    cell = row.createCell((count)+8);
                    Map<String, Object> questionMap = objectMapper.convertValue(subQuestion, Map.class);
                    Integer subQuestionId = objectMapper.convertValue(questionMap.get("sub_question_id"), Integer.class);
                    cell.setCellValue("문항"+questionId+"-"+subQuestionId);
                    cell.setCellStyle(style);
                    count++;
                }
            }
        }
        // 이용자 설문조사 쿼리 작성
        for (String s : answerData.keySet()) {
            // 아이디 = 학교코드 학년 반 담당선생님 번호 이름 게시일
            // 셀 = 담당선생님 이름 학교 학년 반 번호
            System.out.println("s = " + s);
            String[] answerParams = s.split("__");
            Map<String, Object> userAnswerMap = answerData.get(s);
            row = worksheet.createRow(rownum++);
            cell = row.createCell(0);
            cell.setCellValue(answerParams[3]);
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellValue(answerParams[5]);
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue(userAnswerMap.get("school_name").toString());
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellValue(answerParams[1]);
            cell.setCellStyle(style);
            cell = row.createCell(4);
            cell.setCellValue(answerParams[2]);
            cell.setCellStyle(style);
            cell = row.createCell(5);
            cell.setCellValue(answerParams[4]);
            cell.setCellStyle(style);
            cell = row.createCell(6);
            cell.setCellValue(answerParams[6]);
            cell.setCellStyle(style);
            cell = row.createCell(7);
            cell.setCellValue(objectMapper.convertValue(
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
                    cell = row.createCell(count);
                    cell.setCellValue("");
                    cell.setCellStyle(style);
                    count++;
                    continue;
                }
                List<SurveySubQuestion> subAnswers = objectMapper.convertValue(subAnswers_obj.get("answer"), List.class);

                for (Object subQuestion : subQuestions) {
                    cell = row.createCell(count);
                    cell.setCellValue("");
                    cell.setCellStyle(style);
                    Map<String, Object> questionMap = objectMapper.convertValue(subQuestion, Map.class);
                    Integer subQuestionId = objectMapper.convertValue(questionMap.get("sub_question_id"), Integer.class);
                    for (Object answerObj : subAnswers) {
                        SurveySubQuestion answer = objectMapper.convertValue(answerObj, SurveySubQuestion.class);
                        if(answer.getSub_question_id() == subQuestionId) {
                            String answerStr = answer.getSub_question_answer();
                            if(answer.getSub_question_input() != "") answerStr = answerStr + ", " + answer.getSub_question_input();
                            cell.setCellValue(answerStr == null ? "" : answerStr);
                        }
                    }
                    count++;
                }
            }
        }
        // 스타일 작성

        row = worksheet.createRow(0);
        for(int i=0;i<6;i++){
            cell = row.createCell(i);
            cell.setCellValue("기본정보");
            cell.setCellStyle(style);
        }
        for(int i=7;i<count;i++) {
            cell = row.createCell(i);
            cell.setCellValue("설문응답");
            cell.setCellStyle(style);
        }
//        for(int i=0;i<count;i++){
//            worksheet.autoSizeColumn(i);
//            worksheet.setColumnWidth(i, (worksheet.getColumnWidth(i) + 50));
//        }
        worksheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
        worksheet.addMergedRegion(new CellRangeAddress(0,0,6,count-1));
    }
}
