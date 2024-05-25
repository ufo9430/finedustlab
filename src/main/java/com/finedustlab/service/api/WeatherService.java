package com.finedustlab.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.domain.repository.APIRepository;
import com.finedustlab.model.api.WeatherRequestDTO;
import com.finedustlab.model.api.WeatherResponseDTO;
import com.google.cloud.firestore.CollectionReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class WeatherService {

    @Autowired
    private APIRepository apiRepository;

    private final String APIKEY =
            "YN2rq1qN3r2acjV5XdAAy5oSlwnhEbWdmvdMKuTDYtzl9ON60X0G7s8Ub3lGO%2FaOM2AD54fNh3XDK6%2B6MSyBlg%3D%3D";

    public WeatherResponseDTO getWeather(WeatherRequestDTO requestDTO) throws Exception {
        double x = Double.parseDouble(requestDTO.getLat());
        double y = Double.parseDouble(requestDTO.getLng());

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");

        String inputDate = formatter.format(time);
        String dateModulated = getModulatedDate(inputDate);

        String strDate = dateModulated.split("-")[0];
        String strTime = dateModulated.split("-")[1];

        LatXLngY grid = convertGRID_GPS(x,y);

        WeatherResponseDTO output = new WeatherResponseDTO();


        WeatherResponseDTO temp = apiRepository.findWeatherByXYandTime((int) grid.getX(),
                (int) grid.getY(), strTime);
        if(!(temp == null)){
            return temp;
        }

        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"
                + "?serviceKey=" + APIKEY
                + "&dataType=JSON"             // JSON, XML
                + "&numOfRows=60"              // 페이지 ROWS
                + "&pageNo=1"                  // 페이지 번호
                + "&base_date=" + strDate         // 발표일자
                + "&base_time=" + strTime         // 발표시각
                + "&nx=" + (int) grid.getX()                   // 예보지점 X 좌표
                + "&ny=" + (int) grid.getY() ;                 // 예보지점 Y 좌표


        JSONObject data = getData(url);
        JSONObject header = (JSONObject) data.get("header");

        if(header.get("resultCode").equals("00")){
            output.setResult("complete");

            JSONObject body = (JSONObject) data.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            String id = (int) grid.getX() +"-"
                    + (int) grid.getY() +"-"+
                    strTime;
            output.setId(id);
            output.setHumidity(getJsonArrayValue(item,"REH"));
            output.setTemperature(getJsonArrayValue(item,"T1H"));
            output.setDate(inputDate);
            apiRepository.setWeather(output);
        }else{
            output.setResult("error");
        }
        return output;
    }

    private static String getJsonArrayValue(JSONArray item, String category){
        String result = "";
        for (Object o : item) {
            JSONObject object = (JSONObject) JSONValue.parse(o.toString());
            if(object.get("category").equals(category)){
                result = (String) object.get("fcstValue");
                break;
            }
        }
        return result;
    }

    private static String getModulatedDate(String inputDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");

        int time = Integer.parseInt(inputDate.split("-")[1]);
        int minute = time % 100;
        int hour = (time - minute) / 100;

        if(minute < 30) minute = 0;
        else minute = 30;

        Date date = dateFormat.parse(inputDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,minute);
        cal.add(Calendar.MINUTE,-30);
        date = cal.getTime();

        return dateFormat.format(date);
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

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(stringData);
            JSONObject response = (JSONObject) jsonObject.get("response");

            result = response;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }



    private LatXLngY convertGRID_GPS( double lat_X, double lng_Y )
    {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //


        double DEGRAD = Math.PI / 180.0;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        LatXLngY rs = new LatXLngY();

        rs.setLat(lat_X);
        rs.setLng(lng_Y);
        double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = lng_Y * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        rs.setX(Math.floor(ra * Math.sin(theta) + XO + 0.5));
        rs.setY(Math.floor(ro - ra * Math.cos(theta) + YO + 0.5));

        return rs;
    }

    @Getter
    @Setter
    @ToString
    static
    class LatXLngY
    {
        private double lat;
        private double lng;

        private double x;
        private double y;

    }
}
