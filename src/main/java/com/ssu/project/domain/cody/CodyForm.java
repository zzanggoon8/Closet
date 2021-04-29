package com.ssu.project.domain.cody;

import lombok.Data;

@Data
public class CodyForm {

    private Long outerId;
    private Long topId;
    private Long bottomId;
    private Long accId;
    private Long shoesId;

    private String backgroundId;
    private Long outerSize;
    private Long topSize;
    private Long bottomSize;
    private Long shoesSize;
    private Long accSize;

}