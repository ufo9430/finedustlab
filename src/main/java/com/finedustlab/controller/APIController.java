package com.finedustlab.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finedustlab.model.api.APIRequestDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class APIController {

    private final String APIKEY_WHETHER =
            "ISOm1PZB6EqoIW%2FoJwuaLOG%2FCLNiXiqW%2FkTFkJ6CFFXq%2F%2FoDwKuYxZFNUPz3bzdu%2BVkYZsBbh10Y51t3ndF9nw%3D%3D";

    @GetMapping("/whether/get")
    @ResponseBody
    public Map<String,Object> getWeather(@RequestBody APIRequestDTO requestDTO) throws Exception {
        double x = Double.parseDouble(requestDTO.getLat());
        double y = Double.parseDouble(requestDTO.getLng());
        String date = requestDTO.getDate();
        SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyymmdd");

        String time = requestDTO.getTime();
        SimpleTime

        LatXLngY grid = convertGRID_GPS(x,y);

        System.out.println("date = " + date);
        System.out.println("time = " + time);
        System.out.println("grid.toString() = " + grid.toString());

        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"
                + "?serviceKey=" + APIKEY_WHETHER
                + "&dataType=JSON"             // JSON, XML
                + "&numOfRows=60"              // 페이지 ROWS
                + "&pageNo=1"                  // 페이지 번호
                + "&base_date=" + date         // 발표일자
                + "&base_time=" + time         // 발표시각
                + "&nx=" + (int) grid.getX()                   // 예보지점 X 좌표
                + "&ny=" + (int) grid.getY() ;                 // 예보지점 Y 좌표

        System.out.println("url = " + url);


        return getData(url);
    }


    @SuppressWarnings("unchecked")

    private Map<String, Object> getData(String input){
        String stringData;
        Map<String, Object> result = new HashMap<>();
        try{
            URL url = new URL(input);

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

            result = new ObjectMapper().readValue(jsonObject.toJSONString(), Map.class);

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
        double RADDEG = 180.0 / Math.PI;

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
