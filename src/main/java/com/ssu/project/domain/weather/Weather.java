package com.ssu.project.domain.weather;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Weather {
    @Id @GeneratedValue
    private Long id;
    private String baseDate;
    private String meridiem;
    private String city;
    private Long temperature;
    private String localWeather;

    @Builder
    public Weather(String baseDate, String meridiem, String city, Long temperature, String localWeather) {
        this.baseDate = baseDate;
        this.meridiem = meridiem;
        this.city = city;
        this.temperature = temperature;
        this.localWeather = localWeather;
    }
}
