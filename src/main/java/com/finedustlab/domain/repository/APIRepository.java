package com.finedustlab.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class APIRepository {
    Firestore firestore = FirestoreClient.getFirestore();
    private final String API_DATA = "api_data";

    public void save(){

    }
    public Object getFinedustByCityName(String sido, String city) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentSnapshot> future = firestore.collection(API_DATA).document(sido).get();
        DocumentSnapshot documentSnapshot = future.get();
        Object response = documentSnapshot.get("response");
        ObjectMapper objectMapper = new ObjectMapper();
        Map cityMap = objectMapper.convertValue(response, Map.class);

        for (Object o : cityMap.keySet()) {

        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private JSONObject getData(String inputUrl){
        JSONObject result = new JSONObject();
        try{
            URL url = new URL(inputUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader br;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            int BUFF_SIZE = 262144;
            br.mark(BUFF_SIZE);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.reset();
            br.close();
            conn.disconnect();

            JSONParser jsonParser = new JSONParser();
            result = (JSONObject)jsonParser.parse(String.valueOf(sb));
        }catch (Exception e){
            result = null;
        }

        return result;
    }
}
