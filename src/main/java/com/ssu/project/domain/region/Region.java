package com.ssu.project.domain.region;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Region {

    @Id @GeneratedValue
    private Long id;
    private String regionWeatherId;
    private String regionTemperatureId;
    private String city;
}
