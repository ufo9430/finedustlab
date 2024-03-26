package com.finedustlab.service.location;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class LocationService {

    private final String APIKEY_SCHOOL = "93eb3a68b8bc4b61b7be5b5938446899";

    public String getLocation(String schoolCode){
        String result;
        try{
            String rawLoc = getSidoBySchoolCode(schoolCode);
            LocationMaker locationMaker = new LocationMaker();
            String[] splitLoc = rawLoc.split(" ");
            String sido = locationMaker.getModifiedLocation(splitLoc[0]);
            String gungu = splitLoc[1];

            result = sido + "-" + gungu;
        }catch (Exception e){
            result = "error";
        }
        return result;
    }
    public String getSidoBySchoolCode(String schoolCode){
        String url = "https://open.neis.go.kr/hub/schoolInfo" +
                "?KEY=" + APIKEY_SCHOOL+
                "&Type=json" +
                "&pIndex=1" +
                "&pSize=5" +
                "&SD_SCHUL_CODE=" + schoolCode;

        System.out.println("url_school = " + url);

        JSONObject data = getData(url);
        if(data.isEmpty()) return "정보를 찾지 못했습니다";
        JSONArray schoolInfo = (JSONArray) data.get("schoolInfo");
        JSONObject rows = (JSONObject) schoolInfo.get(1);
        JSONArray row = (JSONArray) rows.get("row");
        JSONObject item = (JSONObject) row.get(0);

        if(item.get("ORG_RDNMA")!= null) {
            return (String) item.get("ORG_RDNMA");
        }else{
            return "정보를 찾지 못했습니다";
        }
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

            System.out.println("FinedustOutsideController.getData");
            System.out.println("sb = " + sb);

            JSONParser jsonParser = new JSONParser();
            result = (JSONObject)jsonParser.parse(String.valueOf(sb));
        }catch (Exception e){
            result = null;
        }

        return result;
    }

}
