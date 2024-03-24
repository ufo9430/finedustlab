package com.finedustlab.controller.api;


import com.finedustlab.model.api.WeatherRequestDTO;
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
    @ResponseBody
    public Map<String, Object> getWeather(@RequestBody WeatherRequestDTO requestDTO) throws Exception {
        return weatherController.getWeather(requestDTO);
    }


    @GetMapping("/finestatus/get")
    @ResponseBody
    public Map<String,Object> getFineStatus(@RequestParam @Schema(defaultValue = "중랑구") String location) throws Exception {
        return finedustOutsideController.getFinedustStatus(location);
    }


}
