package com.ssu.project.dto.item;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequestDto {
    @Builder.Default
    private String categoryName = "best";
    @Builder.Default
    private int limit = 20;
    @Builder.Default
    private String sort = "name";
    @Builder.Default
    private int page = 0;
}
