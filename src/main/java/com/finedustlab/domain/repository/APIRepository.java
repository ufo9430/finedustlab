package com.finedustlab.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.model.api.WeatherResponseDTO;
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
    private final String HOLIDAY_DATA = "api_holiday";

    public void setWeather(WeatherResponseDTO weather) {
        firestore.collection(WEATHER_DATA).add(weather);
    }
    @SuppressWarnings("unchecked")
    public void setFinedustData(String sido, JSONObject data) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference document = firestore.collection(FINEDUST_DATA).document(sido);
        document.set(data);
    }
    public void setHoliday(Map<String, Object> holidayData){
        firestore.collection(HOLIDAY_DATA).document("data").set(holidayData);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getHolidayData(String date) throws ExecutionException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        ApiFuture<DocumentSnapshot> future = firestore.collection(HOLIDAY_DATA).document("data").get();
        DocumentSnapshot documentSnapshot = future.get();
        Map<String, Object> data = documentSnapshot.getData();
        for (String s : data.keySet()) {
            Object o = data.get(s);
            HashMap<String, Object> holiday = objectMapper.convertValue(o, HashMap.class);
            if(String.valueOf(holiday.get("locdate")).equals(date)){
                return holiday;
            }
        }
        return null;
    }

    public WeatherResponseDTO findWeatherByXYandTime(int x, int y, String time){
        String id = x +"-" + y +"-"+ time;
        ObjectMapper objectMapper = new ObjectMapper();
        CollectionReference tempData = firestore.collection(WEATHER_DATA);
        WeatherResponseDTO result;
        try{
            QuerySnapshot survey = tempData.whereEqualTo("id", id).get().get();
            QueryDocumentSnapshot document = survey.getDocuments().get(0);
            result = objectMapper.convertValue(document.getData(), WeatherResponseDTO.class);
            return result;
        }catch (Exception ignored){
            return null;
        }
    }

    public Map<String,String> getFinedustByCityName(String sido, String city) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> future = firestore.collection(FINEDUST_DATA).document(sido).get();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,String> finedust = new HashMap<>();


        DocumentSnapshot documentSnapshot = future.get();
        Object response = documentSnapshot.get("response");
        JSONObject responseMap = objectMapper.convertValue(response, JSONObject.class);
        Object body = responseMap.get("body");
        JSONObject bodyJson = objectMapper.convertValue(body, JSONObject.class);
        Object items = bodyJson.get("items");
        JSONArray itemsArray = objectMapper.convertValue(items, JSONArray.class);

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
