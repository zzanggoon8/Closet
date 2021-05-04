package com.ssu.project.service.daily;


import org.springframework.stereotype.Service;

@Service
public class CityNameService {
    private String rename ="";

    public String renameCity(String cityName){
        switch (cityName) {
            case "서울특별시": rename="서울_인천_경기도";
                break;
            case "인천광역시": rename="서울_인천_경기도";
                break;
            case "경기도": rename="서울_인천_경기도";
                break;

            case "부산광역시": rename="부산_울산_경상남도";
                break;
            case "경상남도": rename="부산_울산_경상남도";
                break;
            case "울산광역시": rename="부산_울산_경상남도";
                break;

            case "대구광역시": rename="대구_경상북도";
                break;
            case "경상북도": rename="대구_경상북도";
                break;

            case "광주광역시": rename="광주_전라남도";
                break;
            case "전라남도": rename="광주_전라남도";
                break;
            case "대전광역시": rename="대전_세종_충청남도";
                break;
            case "충청남도": rename="대전_세종_충청남도";
                break;
            case "제주특별자치도": rename="제주도";
                break;
        }

        return rename;
    }
}
