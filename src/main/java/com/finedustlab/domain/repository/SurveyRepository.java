package com.finedustlab.domain.repository;

import com.finedustlab.model.SurveyAnswerDto;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

@Repository
public class SurveyRepository {
    public static final String SURVEY_ANSWER = "survey_answer";
    private static final String SURVEY_DATA = "survey_data";

    FirebaseDatabase db = FirebaseDatabase.getInstance();

    public String save(String document_id, SurveyAnswerDto answer) {
        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection(SURVEY_ANSWER).document(document_id)
                .set(answer,SetOptions.merge());
        return document_id;
    }

    public Object findDataByID(String id){
        SurveyAnswerDto result;
        Firestore firestore = FirestoreClient.getFirestore();
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
