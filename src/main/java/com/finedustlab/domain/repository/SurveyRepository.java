package com.finedustlab.domain.repository;

import com.finedustlab.model.Survey;
import com.google.api.client.json.Json;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import org.apache.catalina.connector.Response;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SurveyRepository {
    public static final String SURVEY_ANSWER = "survey_answer";
    private static final String SURVEY_DATA = "survey_data";

    FirebaseDatabase db = FirebaseDatabase.getInstance();

    public void save(HashMap<String, Survey> answer) {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference surveys = firestore.collection(SURVEY_ANSWER);
        surveys.document("2").set(answer);
    }

    public Response findByID(String id) {
        Response result;
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference survey = firestore.collection(SURVEY_DATA);
        ApiFuture<DocumentSnapshot> future = survey.document(id).get();
        try{
            DocumentSnapshot documentSnapshot = future.get();
            result = documentSnapshot.toObject(Response.class);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
