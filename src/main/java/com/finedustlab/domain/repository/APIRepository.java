package com.finedustlab.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.model.survey.SurveyAnswer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class APIRepository {
    Firestore firestore = FirestoreClient.getFirestore();
    private final String FINEDUST_DATA = "api_finedust";
    private final String WEATHER_DATA = "api_weather";

    public void saveWeather(Map weather){
        String time = (String) weather.get("time");
        ApiFuture<DocumentSnapshot> future = firestore.collection(WEATHER_DATA).document().get();

    }

    public Object findWeatherByXYandDate(double x, double y,String date){
        SurveyAnswer result;
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference surveyData = firestore.collection(WEATHER_DATA);
        try{
            QuerySnapshot survey = surveyData.whereEqualTo("content_type", date).get().get();
            QueryDocumentSnapshot document = survey.getDocuments().get(0);
            return document.getData();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
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
}
