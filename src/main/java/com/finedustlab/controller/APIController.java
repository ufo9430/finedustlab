package com.finedustlab.controller;


import com.finedustlab.domain.repository.APIRepository;
import com.finedustlab.model.api.LocalFinedustResponseDTO;
import com.finedustlab.model.api.WeatherRequestDTO;
import com.finedustlab.model.api.WeatherResponseDTO;
import com.finedustlab.service.api.FinedustLocalService;
import com.finedustlab.service.api.HolidayService;
import com.finedustlab.service.api.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
public class APIController {
    @Autowired
    WeatherService weatherService;
    @Autowired
    FinedustLocalService finedustOutsideService;
    @Autowired
    HolidayService holidayService;

    @Autowired
    APIRepository apiRepository;

    @GetMapping("/weather/get")
    @Operation(description = "위도/경도와 현재 날짜를 입력하여 상태 신호 result와 temperature, humidity를 받습니다.")
    @ResponseBody
    public WeatherResponseDTO getWeather(@RequestBody WeatherRequestDTO requestDTO) throws Exception {
        return weatherService.getWeather(requestDTO);
    }

    @GetMapping("/finestatus/get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "미세먼지 조회 성공", content = @Content(schema = @Schema(implementation = LocalFinedustResponseDTO.class)))})
    @Operation(description = "학교 코드를 입력받아 해당 지역 시군구 측정소의 미세먼지 지수와 상태를 불러옵니다. 미세먼지 지수 45 이하일 경우 good, 이상일 경우 bad가 반환됩니다. ")
    @ResponseBody
    public LocalFinedustResponseDTO getFinedustStatus(@RequestParam @Schema(defaultValue = "7201099") String location) throws Exception {
        return finedustOutsideService.getFinedustStatus(location);
    }


    @GetMapping("/holiday/get")
    @Operation(description = "요청 당시 월요일부터 다음주 일요일 사이의 공휴일 데이터를 불러옵니다.")
    @ResponseBody
    public Map<String, Object> getHolidayInfo(){
        return holidayService.getHolidayInfo();
    }

    @Scheduled(cron = "0 0 6 * * *")
    private void clearData() throws UnsupportedEncodingException {
        apiRepository.deleteCollection("api_weather");
        apiRepository.deleteCollection("api_finedust");
        apiRepository.deleteCollection("api_holiday");
        finedustOutsideService.saveData();
        holidayService.setHolidayInfo();
    }

}
