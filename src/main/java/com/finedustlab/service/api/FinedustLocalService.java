package com.finedustlab.service.api;

import com.finedustlab.domain.repository.APIRepository;
import com.finedustlab.model.api.LocalFinedustResponseDTO;
import com.finedustlab.service.api.location.LocationMapper;
import com.finedustlab.service.api.location.LocationService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
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
            "YN2rq1qN3r2acjV5XdAAy5oSlwnhEbWdmvdMKuTDYtzl9ON60X0G7s8Ub3lGO%2FaOM2AD54fNh3XDK6%2B6MSyBlg%3D%3D";

    @Autowired
    private LocationService locationService;

    @Autowired
    private APIRepository apiRepository;

    @SuppressWarnings("unchecked")
    public LocalFinedustResponseDTO getFinedustStatus(String schoolCode) throws IOException, InterruptedException, ExecutionException {

        LocalFinedustResponseDTO result = new LocalFinedustResponseDTO();
        Map<String, String> fine_data;
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

        System.out.println("sido = " + sido);
        System.out.println("city = " + city);


        fine_data = apiRepository.getFinedustByCityName(sido,city);
        result.setFinedust_factor(fine_data.get("finedust_factor"));
        result.setUltrafine_factor(fine_data.get("ultrafine_factor"));

        int finedust_factor = Integer.parseInt(result.getFinedust_factor());
        int ultrafine_factor = Integer.parseInt(result.getUltrafine_factor());

        if(finedust_factor < 0
                || ultrafine_factor < 0){
            return getErrorResult("getFinedustStatus 미세먼지 값을 불러오지 못했습니다.");
        }

        String status;
        if(finedust_factor <= 30 && finedust_factor>=0){
            status = "good";
        }else if(finedust_factor <= 150){
            status = "fine";
        }else{
            status = "bad";
        }
        result.setFine_status(status);

        if(ultrafine_factor <= 15 && ultrafine_factor>=0){
            status = "good";
        }else if(ultrafine_factor <= 75){
            status = "fine";
        }else{
            status = "bad";
        }
        result.setUltra_status(status);


        result.setResult("complete");

        return result;
    }

    public void saveData() throws UnsupportedEncodingException {

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


    private static LocalFinedustResponseDTO getErrorResult(String str) {
        LocalFinedustResponseDTO result = new LocalFinedustResponseDTO();
        result.setResult("error");
        result.setFine_status("-");
        result.setUltra_status("-");
        System.out.println("error사유 = " + str);
        return result;
    }


    private JSONObject getData(String inputUrl){
        JSONObject result;
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
