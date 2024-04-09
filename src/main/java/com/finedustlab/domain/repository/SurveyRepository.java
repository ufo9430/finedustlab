package com.finedustlab.domain.repository;

import com.finedustlab.model.survey.SurveyAnswer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SurveyRepository {
    public static final String SURVEY_ANSWER = "survey_answer";
    private static final String SURVEY_DATA = "survey_data";
    Firestore firestore = FirestoreClient.getFirestore();

    public String save(String document_id, SurveyAnswer answer) {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put(String.valueOf(answer.getAnswer_id()),setAnswerData(answer));
        firestore.collection(SURVEY_ANSWER).document(document_id)
                .set(fields,SetOptions.merge());
        return document_id;
    }

    private HashMap<String, String> setAnswerData(SurveyAnswer answer){
        HashMap<String, String> result = new HashMap<>();
        result.put("answer",answer.getAnswer());
        result.put("sub_answer",answer.getSub_answer());
        result.put("type",answer.getType());
        return result;
    }

    public Map<String, Map<String, Object>> findAllAnswerData(){
        CollectionReference surveyAnswer = firestore.collection(SURVEY_ANSWER);
        Map<String, Map<String, Object>> answers = new HashMap<>();
        try{
            ApiFuture<QuerySnapshot> future = surveyAnswer.get();
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
