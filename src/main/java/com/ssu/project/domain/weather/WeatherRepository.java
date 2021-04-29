package com.ssu.project.domain.weather;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository <Weather, Long> {
    @Query("SELECT w FROM Weather w ORDER BY w.id ASC")
    List<Weather> findAllDesc();

    //    @Query(value = "SELECT * FROM WEATHER WHERE (MERIDIEM LIKE '%mer%') AND (CITY LIKE '%서%') AND (BASE_DATE BETWEEN '20210325' AND '20210402')", nativeQuery = true)
//    List <Weather> findByNameLike(String city);

    @Query(value = "select * from  WEATHER w where w.meridiem = :meridiem and w.city like %:weatherCity% and w.BASE_DATE BETWEEN :startDate and :endDate", nativeQuery = true)
    List<Weather> findByWeatherCityAndBaseDateAndMeridiemOrderByBaseDateDesc(
            @Param("weatherCity") String weatherCity,
            @Param("meridiem") String meridiem,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    //원하는 지역만 찾을때 사용
    @Query(value = "select * from  WEATHER e where e.city like %:city% ", nativeQuery = true)
    List<Weather> findByCurrentLocalWeather(@Param("city") String city);

    // 날짜, 지역, 오전/오후인지와 일치하는 값 뽑기
    @Query(value = "select * from  WEATHER e where e.base_date like %:base_date%  and e.city like %:city% and e.meridiem like %:meridiem%", nativeQuery = true)
    List<Weather> findCurrentDateTemperature(@Param("base_date") String currentDate,
                                             @Param("city") String city,
                                             @Param("meridiem") String meridiem
    );
}
