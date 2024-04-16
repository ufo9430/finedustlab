package com.finedustlab.service.api;

import com.finedustlab.domain.repository.APIRepository;
import com.finedustlab.service.api.location.LocationMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HolidayService {
    private final String APIKEY =
            "ISOm1PZB6EqoIW%2FoJwuaLOG%2FCLNiXiqW%2FkTFkJ6CFFXq%2F%2FoDwKuYxZFNUPz3bzdu%2BVkYZsBbh10Y51t3ndF9nw%3D%3D";
    @Autowired
    private APIRepository apiRepository;
    public void setHolidayInfo(){
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        String year = formatter.format(time);

        String url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"
                +"?serviceKey="+APIKEY
                +"&pageNo=1"
                +"&numOfRows=100"
                +"&solYear="+year;
        JSONObject data = getData(url);
        JSONObject header = (JSONObject) data.get("header");

        Map<String,Object> output = new HashMap<>();
        if(header.get("resultCode").equals("00")){
            output.put("result","complete");

            JSONObject body = (JSONObject) data.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");
            Map<String, Object> holidayMap = new HashMap<>();
            List<Object> list = item.toList();
            int i=0;

            for (Object o : list) {
                holidayMap.put(String.valueOf(i++),o);
            }

            apiRepository.setHoliday(holidayMap);
        }
    }

    public Map<String, Object> getHolidayInfo(){
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DayOfWeek dayOfWeek = time.getDayOfWeek();
        int dayOfWeekNum = dayOfWeek.getValue();
        LocalDateTime firstDayOfWeek = time.plusDays(-dayOfWeekNum);

        Map<String, Object> result = new HashMap<>();

        ArrayList<Object> dataArray = new ArrayList<>();

        for(int i=0;i<14;i++){
            LocalDateTime date = firstDayOfWeek.plusDays(i);
            String strDate = formatter.format(date);
            try{
                Map<String, Object> holidayData = apiRepository.getHolidayData(strDate);
                if(!holidayData.isEmpty()) {
                    dataArray.add(holidayData);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        result.put("result",dataArray);

        return result;
    }

    private static Map<String, Object> getErrorResult(String date) {
        Map<String, Object> result = new HashMap<>();
        result.put("datekind", "01");
        result.put("isholiday","N");
        result.put("locdate",Integer.parseInt(date));
        result.put("dateName","공휴일 정보가 없습니다.");
        return result;
    }

    @Scheduled(cron = "* 0 * * * *")
    private void saveData(){
        apiRepository.deleteCollection("api_holiday");
        setHolidayInfo();
    }


    private JSONObject getData(String urlInput){
        String stringData;
        JSONObject result = null;
        try{
            URL url = new URL(urlInput);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader br;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            stringData = br.readLine();

            JSONObject jsonObject = XML.toJSONObject(stringData);
            JSONObject response = (JSONObject) jsonObject.get("response");

            result = response;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

}
