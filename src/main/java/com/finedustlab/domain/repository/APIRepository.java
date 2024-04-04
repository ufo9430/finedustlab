package com.finedustlab.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class APIRepository {
    Firestore firestore = FirestoreClient.getFirestore();
    private final String FINEDUST_DATA = "api_finedust";
    private final String WEATHER_DATA = "api_weather";

    public void setWeather(Map<String, Object> weather) throws ExecutionException, InterruptedException {
        firestore.collection(WEATHER_DATA).add(weather);
    }

    public Map<String, Object> findWeatherByXYandTime(int x, int y, String time){
        String id = x +"-" + y +"-"+ time;
        ObjectMapper objectMapper = new ObjectMapper();
        CollectionReference tempData = firestore.collection(WEATHER_DATA);
        Map result = new HashMap<>();
        try{
            QuerySnapshot survey = tempData.whereEqualTo("id", id).get().get();
            QueryDocumentSnapshot document = survey.getDocuments().get(0);
            result = objectMapper.convertValue(document.getData(), Map.class);
        }catch (Exception ignored){}
        return result;
    }

    @SuppressWarnings("unchecked")
    public void setFinedustData(String sido, JSONObject data) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference document = firestore.collection(FINEDUST_DATA).document(sido);
        document.set(data);
    }
    @SuppressWarnings("unchecked")
    public Map<String,String> getFinedustByCityName(String sido, String city) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> future = firestore.collection(FINEDUST_DATA).document(sido).get();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,String> finedust = new HashMap();


        DocumentSnapshot documentSnapshot = future.get();
        Object response = documentSnapshot.get("response");
        JSONObject responseMap = objectMapper.convertValue(response, JSONObject.class);
        Object body = responseMap.get("body");
        JSONObject bodyJson = objectMapper.convertValue(body, JSONObject.class);
        Object items = bodyJson.get("items");
        JSONArray itemsArray = objectMapper.convertValue(items, JSONArray.class);
        System.out.println("itemsArray = " + itemsArray);

        for (Object item : itemsArray) {
            JSONObject itemJson = objectMapper.convertValue(item, JSONObject.class);
            String itemCityName = (String) itemJson.get("cityName");
            if(itemCityName.equals(city)){
                finedust.put("finedust_factor",(String) itemJson.get("pm10Value"));
                finedust.put("ultrafine_factor",(String) itemJson.get("pm25Value"));
                break;
            }
        }

        if(finedust.isEmpty()){
            finedust.put("finedust_factor","-1");
            finedust.put("ultrafine_factor","-1");
        }

        return finedust;
    }

    /**
     * Delete a collection in batches to avoid out-of-memory errors. Batch size may be tuned based on
     * document size (atmost 1MB) and application requirements.
     */
    public void deleteCollection(String name) {
        CollectionReference collection = firestore.collection(name);
        try {
            // retrieve a small batch of documents to avoid out-of-memory errors
            ApiFuture<QuerySnapshot> future = collection.get();
            int deleted = 0;
            // future.get() blocks on document retrieval
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
            }
        } catch (Exception e) {
            System.err.println("Error deleting collection : " + e.getMessage());
        }
    }
}
