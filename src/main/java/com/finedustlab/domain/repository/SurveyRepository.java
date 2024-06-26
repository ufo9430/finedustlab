package com.finedustlab.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.model.survey.SurveyAnswer;
import com.finedustlab.model.user.StudentProfile;
import com.finedustlab.model.user.UserProfile;
import com.finedustlab.service.api.location.LocationService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class SurveyRepository {
    public static final String SURVEY_ANSWER = "survey_answer";
    private static final String SURVEY_DATA = "survey_data";

    @Autowired private LocationService locationService;
    Firestore firestore = FirestoreClient.getFirestore();

    ObjectMapper objectMapper = new ObjectMapper();

    public String save(StudentProfile profile, SurveyAnswer answer, String teacherName){
        HashMap<String, Object> fields = new HashMap<>();
        String[] dateSplit = answer.getDate().split("T");
        String date_day = dateSplit[0];
        String date_time;
        if(dateSplit.length == 1){
            date_time = "-";
        }else{
            date_time = dateSplit[1].replace("Z","");
        }
        String school_name = locationService.getSchoolNameBySchoolCode(String.valueOf(profile.getSchool_code()));
        String document_id =
                profile.getSchool_code()+"__"+
                        profile.getGrade()+"__"+
                        profile.getClass_num()+"__"+
                        teacherName + "__" +
                        profile.getStudent_num()+"__"+
                        profile.getName()+"__"+
                        date_day;

        fields.put("profile",profile);
        fields.put("date",date_day);
        fields.put("time",date_time);
        fields.put("school_name",school_name);
        fields.put(String.valueOf(answer.getQuestion_id()), setAnswerData(answer));
        firestore.collection(SURVEY_ANSWER).document(document_id)
                .set(fields,SetOptions.merge());

        return document_id;
    }

    private HashMap<String, Object> setAnswerData(SurveyAnswer answer){
        HashMap<String, Object> result = new HashMap<>();
        result.put("answer",answer.getAnswers());
        return result;
    }

    public Map<String, Map<String, Object>> findAnswerDataByUserType(String userType){
        CollectionReference surveyAnswer = firestore.collection(SURVEY_ANSWER);
        Map<String, Map<String, Object>> answers = new HashMap<>();
        try{
            FieldPath fieldPath = FieldPath.of("profile","user_type");
            ApiFuture<QuerySnapshot> future = surveyAnswer.whereEqualTo(fieldPath,userType).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Map<String, Object> data = document.getData();
                answers.put(document.getId(), data);
            }
            return answers;
        }catch (Exception e){
            e.printStackTrace();
            return answers;
        }
    }

    public Map<String, Map<String, Object>> findAnswerDataBySchoolInfo(String schoolCode, String grade, String class_num, String userType){
        CollectionReference surveyAnswer = firestore.collection(SURVEY_ANSWER);
        Map<String, Map<String, Object>> answers = new HashMap<>();
        try{
            FieldPath fieldPath = FieldPath.of("profile","school_code");
            ApiFuture<QuerySnapshot> future = surveyAnswer.whereEqualTo(fieldPath,Integer.parseInt(schoolCode)).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                Map<String, Object> data = document.getData();
                UserProfile userProfile = objectMapper.convertValue(data.get("profile"), StudentProfile.class);

                if(String.valueOf(userProfile.getGrade()).equals(grade) &&
                String.valueOf(userProfile.getClass_num()).equals(class_num) &&
                userProfile.getUser_type().equals(userType)){
                    answers.put(document.getId(), data);
                }
            }
            return answers;
        }catch (Exception e){
            e.printStackTrace();
            return answers;
        }
    }

    public Object findDataByID(String id){
        CollectionReference surveyData = firestore.collection(SURVEY_DATA);
        try{
            QuerySnapshot survey = surveyData.whereEqualTo("content_type", id).get().get();
            QueryDocumentSnapshot document = survey.getDocuments().get(0);
            return document.getData();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
