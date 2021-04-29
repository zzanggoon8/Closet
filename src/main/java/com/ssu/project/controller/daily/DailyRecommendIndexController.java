package com.ssu.project.controller.daily;

import com.ssu.project.service.daily.CityNameService;
import com.ssu.project.service.daily.DateService;
import com.ssu.project.service.item.ItemService;
import com.ssu.project.service.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class DailyRecommendIndexController {

    private final ItemService itemService;
    private final DateService dateService;
    private final CityNameService cityNameService;
    private final WeatherService weatherService;

    @GetMapping("/daily-recommend")
    public String daily_recommend(Model model, String city){
        //String weatherCity = "서울_인천_경기도";;

        String baseDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String meridien = dateService.currentHour();

        if (city != null) {

            String cityName = cityNameService.renameCity(city);
            model.addAttribute("currentTemperature", weatherService.findCurrentDateTemperature(baseDate, cityName, meridien));
            model.addAttribute("weatherList", weatherService.findCurrentLocalWeather(cityName));
            System.out.println("주소가 null 아님");

        }
        else{
            model.addAttribute("currentTemperature", weatherService.findCurrentDateTemperature(baseDate, "서울_인천_경기도", meridien));
            System.out.println("주소가 null일 경우");
        }

        //아우터
        Long parent1 = Long.valueOf("12");
        Long child1 = Long.valueOf("29");
        //상의
        Long parent2 = Long.valueOf("3");
        Long child2 = Long.valueOf("2");

        //하의
        Long parent3 = Long.valueOf("31");
        Long child3 = Long.valueOf("36");

        //아우터 가져오기
        model.addAttribute("outer", itemService.findRecommendCategory(parent1, child1));
        // 상의 가져오기
        model.addAttribute("top", itemService.findRecommendCategory(parent2, child2));
        //하의 가져오기
        model.addAttribute("bottom", itemService.findRecommendCategory(parent3, child3));

        //  System.out.println("현재 온도 : " + temperature);
        return "/view/daily-recommend";
    }
}
