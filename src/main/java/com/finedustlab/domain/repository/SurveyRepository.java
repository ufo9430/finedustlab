package com.finedustlab.domain.repository;

import com.finedustlab.model.Survey;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyRepository {
    public static final String SURVEY_ANSWER = "survey_answer";
    private static final String SURVEY_DATA = "survey_data";

    FirebaseDatabase db = FirebaseDatabase.getInstance();

    public void save(Map<String, String> answer) {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference surveys = firestore.collection(SURVEY_ANSWER);
        surveys.document("2").set(answer);
    }

    public Object findDataByID(String id){
        Survey result;
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference survey = firestore.collection(SURVEY_DATA);
        try{
            QuerySnapshot content = survey.whereEqualTo("content_type", id).get().get();
            QueryDocumentSnapshot document = content.getDocuments().get(0);
            return document.getData();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
