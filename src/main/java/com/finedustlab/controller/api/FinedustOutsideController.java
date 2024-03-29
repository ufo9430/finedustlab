package com.finedustlab.controller.api;

import com.finedustlab.service.location.LocationService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

@Controller
public class FinedustOutsideController {
    private final String APIKEY =
            "ISOm1PZB6EqoIW%2FoJwuaLOG%2FCLNiXiqW%2FkTFkJ6CFFXq%2F%2FoDwKuYxZFNUPz3bzdu%2BVkYZsBbh10Y51t3ndF9nw%3D%3D";

    @Autowired
    private LocationService locationService;

    @SuppressWarnings("unchecked")
    public Map<String, Object> getFinedustStatus(String schoolCode) throws IOException, InterruptedException {

        Map<String, Object> result = new JSONObject();
        String sido, city, finedust;

        // ----지역 정보 가져오기----
        String[] loc = locationService.getLocation(schoolCode).split("-");
        if(loc[0].equals("error")){
            return getErrorResult(result,"locationService 지역 정보를 불러오지 못했습니다.");
        }else{
            sido = loc[0];
            city = loc[1];
        }
        // ----------------------


        String url = "http://apis.data.go.kr/B552584/ArpltnStatsSvc/getCtprvnMesureSidoLIst"
                + "?serviceKey=" + APIKEY
                + "&returnType=json"
                + "&numOfRows=100"
                + "&pageNo=1"
                + "&sidoName=" + URLEncoder.encode(sido, "UTF-8")
                + "&searchCondition=" + "HOUR";

        System.out.println("url_finedust = " + url);


        JSONObject data = getData(url);
        if(data == null){
            Thread.sleep(3000);
            return getFinedustStatus(schoolCode);
//            return getErrorResult(result, "getData 데이터를 얻지 못했습니다.");
        }

        JSONObject response = (JSONObject) data.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONArray items = (JSONArray) body.get("items");

        JSONObject cityInfo = new JSONObject();
        for (Object item : items) {
            JSONObject obj = (JSONObject) JSONValue.parse(item.toString());
            String objCityName = (String) obj.get("cityName");
            if(objCityName.equals(city)){
                cityInfo = obj;
                break;
            }
        }
        if(cityInfo.isEmpty()) return getErrorResult(result, "cityInfo 도시 정보가 검색되지 않았습니다.");

        finedust = (String) cityInfo.get("khaiValue");
        result.put("finedust_level",finedust);

        if(finedust.isEmpty()){
            finedust = "45";
        }

        if(Integer.parseInt(finedust) < 45){
            result.put("status", "good");
        }else{
            result.put("status","bad");
        }
        result.put("result","complete");

        return result;
    }

    private static Map<String, Object> getErrorResult(Map<String, Object> result, String str) {
        result.put("result", "error");
        result.put("finedust_level","-");
        result.put("status",str);
        return result;
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
