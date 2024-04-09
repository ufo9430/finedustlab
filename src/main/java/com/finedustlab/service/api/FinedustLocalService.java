package com.finedustlab.service.api;

import com.finedustlab.domain.repository.APIRepository;
import com.finedustlab.service.api.location.LocationMapper;
import com.finedustlab.service.api.location.LocationService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FinedustLocalService {
    private final String APIKEY =
            "ISOm1PZB6EqoIW%2FoJwuaLOG%2FCLNiXiqW%2FkTFkJ6CFFXq%2F%2FoDwKuYxZFNUPz3bzdu%2BVkYZsBbh10Y51t3ndF9nw%3D%3D";

    @Autowired
    private LocationService locationService;

    @Autowired
    private APIRepository apiRepository;

    @SuppressWarnings("unchecked")
    public Map<String, String> getFinedustStatus(String schoolCode) throws IOException, InterruptedException, ExecutionException {

        Map<String, String> result = new JSONObject();
        String sido, city;

        // ----지역 정보 가져오기----
        String[] loc = locationService.getLocation(schoolCode).split("-");
        if(loc[0].equals("error")){
            return getErrorResult("locationService 지역 정보를 불러오지 못했습니다.");
        }else{
            sido = loc[0];
            city = loc[1];
        }
        // ----------------------

        result = apiRepository.getFinedustByCityName(sido,city);

        if(Integer.parseInt(result.get("finedust_factor")) < 0
                || Integer.parseInt(result.get("ultrafine_factor")) < 0){
            return getErrorResult("getFinedustStatus 미세먼지 값을 불러오지 못했습니다.");
        }
        int finedust_factor = Integer.parseInt(result.get("finedust_factor"));

        if(finedust_factor < 45){
            result.put("status", "good");
        }else{
            result.put("status","bad");
        }
        result.put("result","complete");

        return result;
    }

    @Scheduled(cron = "* 30 * * * *", zone = "Asia/Seoul")
    private void saveData() throws UnsupportedEncodingException {
        LocationMapper locationMapper = new LocationMapper();
        HashMap<String, String> locationPair = locationMapper.getLocationPair();
        for (String s : locationPair.keySet()) {
            saveFinedustData(locationPair.get(s));
        }
    }

    private void saveFinedustData(String sido) throws UnsupportedEncodingException {
        String url = "http://apis.data.go.kr/B552584/ArpltnStatsSvc/getCtprvnMesureSidoLIst"
                + "?serviceKey=" + APIKEY
                + "&returnType=json"
                + "&numOfRows=100"
                + "&pageNo=1"
                + "&sidoName=" + URLEncoder.encode(sido, "UTF-8")
                + "&searchCondition=" + "HOUR";


        JSONObject data = getData(url);

        apiRepository.setFinedustData(sido, data);
    }


    private static Map<String, String> getErrorResult( String str) {
        Map<String, String> result = new HashMap<>();
        result.put("result", "error");
        result.put("finedust_factor","-");
        result.put("ultrafine_factor","-");
        result.put("status",str);
        return result;
    }


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
