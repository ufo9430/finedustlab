package com.finedustlab.controller.api;


import com.finedustlab.model.api.WeatherRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class APIControllerWrapper {
    @Autowired WeatherController weatherController;
    @Autowired FinedustOutsideController finedustOutsideController;

    @GetMapping("/weather/get")
    @Operation(description = "위도/경도와 현재 날짜를 입력하여 상태 신호 result와 temperature, humidity를 받습니다.")
    @ResponseBody
    public Map<String, Object> getWeather(@RequestBody WeatherRequestDTO requestDTO) throws Exception {
        return weatherController.getWeather(requestDTO);
    }


    @GetMapping("/finestatus/get")
    @Operation(description = "학교 코드를 입력받아 해당 지역 시군구 측정소의 미세먼지 지수와 상태를 불러옵니다. 미세먼지 지수 45 이하일 경우 good, 이상일 경우 bad가 반환됩니다. ")
    @ResponseBody
    public Map<String,Object> getFineStatus(@RequestParam @Schema(defaultValue = "7201099") String location) throws Exception {
        return finedustOutsideController.getFinedustStatus(location);
    }


}
