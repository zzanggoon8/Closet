package com.ssu.project.controller.item;

import com.ssu.project.domain.paginator.Paginator;
import com.ssu.project.dto.item.ItemRequestDto;
import com.ssu.project.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ItemIndexController {
    private final ItemService itemService;
    /**
     * 카테고리 아이템 조회
     * @param itemRequestDto
     * @param model
     * @return
     */
    @GetMapping("/itemList")
    public String itemList(ItemRequestDto itemRequestDto, Model model) {
        String categoryName = itemRequestDto.getCategoryName();
        Pageable pageable = itemService.getPageable(itemRequestDto);

        Paginator paginator = new Paginator(5, itemRequestDto.getLimit(), itemService.getCountItemListByCategory(categoryName));
        Map<String, Object> pageInfo = paginator.getFixedBlock(itemRequestDto.getPage());
//        Map<String, Object> pageInfo = paginator.getElasticBlock(itemRequestDto.getPage());
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("itemList", itemService.getItemListByCategory(categoryName, pageable));
        model.addAttribute("categoryName", categoryName);

        return "/view/category";
    }
}
