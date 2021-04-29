package com.ssu.project.domain.region;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    public Region findByRegionTemperatureIdLike(String regionTemperatureId);
    public Region findByRegionWeatherIdLike(String regionWeatherId);
}
