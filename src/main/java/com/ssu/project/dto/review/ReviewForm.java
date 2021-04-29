package com.ssu.project.dto.review;

import lombok.Data;

@Data
public class ReviewForm {
    private Long itemId;
    private Long reviewId; //수정일 경우 존재
    private String title;
    private String contents;
}
