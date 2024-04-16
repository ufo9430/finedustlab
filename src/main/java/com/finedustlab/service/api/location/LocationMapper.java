package com.finedustlab.service.api.location;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class LocationMapper {
    private HashMap<String, String> locationPair = new HashMap<>();
    public LocationMapper() {
        locationPair.put("강원특별자치도","강원");
        locationPair.put("경기도","경기");
        locationPair.put("광주광역시","광주");
        locationPair.put("대구광역시","대구");
        locationPair.put("대전광역시","대전");
        locationPair.put("부산광역시","부산");
        locationPair.put("서울특별시","서울");
        locationPair.put("세종특별자치시","세종");
        locationPair.put("울산광역시","울산");
        locationPair.put("인천광역시","인천");
        locationPair.put("전라남도","전남");
        locationPair.put("전라북도","전북");
        locationPair.put("경상남도","경남");
        locationPair.put("경상북도","경북");
        locationPair.put("전북특별자치도","전북");
        locationPair.put("충청남도","충남");
        locationPair.put("충청북도","충북");
        locationPair.put("제주특별자치도","제주");

    }

    public String getModifiedLocation(String location){
        return locationPair.get(location);
    }
}
