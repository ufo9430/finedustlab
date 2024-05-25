package com.finedustlab.domain.repository;

import com.finedustlab.model.survey.SurveyAnswer;
import com.finedustlab.model.user.StudentProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SurveyRepository {
    public static final String SURVEY_ANSWER = "survey_answer";
    private static final String SURVEY_DATA = "survey_data";
    Firestore firestore = FirestoreClient.getFirestore();

    public String save(StudentProfile profile, SurveyAnswer answer) {
        HashMap<String, Object> fields = new HashMap<>();
        String date_day = answer.getDate().split("T")[0];
        String document_id =
                profile.getSchool_code()+"-"+
                        profile.getGrade()+"-"+
                        profile.getClass_num()+"-"+
                        profile.getStudent_num()+"-"+
                        profile.getName()+"-"+
                        date_day;

        fields.put("profile",profile);
        fields.put("date",date_day);
        fields.put(String.valueOf(answer.getQuestion_id()),setAnswerData(answer));
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
