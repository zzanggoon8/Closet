package com.ssu.project.controller.like;

import com.google.gson.JsonObject;
import com.ssu.project.domain.item.Item;
import com.ssu.project.domain.member.CurrentUser;
import com.ssu.project.domain.member.Member;
import com.ssu.project.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LikeIndexController {
    private final LikeService likeService;

    @GetMapping("/store/like")
    @ResponseBody
    public String addLike(@CurrentUser Member member, @RequestParam("id") Long itemId) {

        boolean result = false;

        JsonObject jsonObject = new JsonObject();

        try {
            result = likeService.addLike(member, itemId);
            // 찜 목록 추가
            if (result) {
                jsonObject.addProperty("message", "Add like list Complelte!");
            } else {
                jsonObject.addProperty("message", "Delete from like list.");
            }
            jsonObject.addProperty("status", result);
        } catch (IllegalArgumentException e) {
            jsonObject.addProperty("message", "Wrong access.");
        } catch (UsernameNotFoundException e) {}

        return jsonObject.toString();
    }

    @GetMapping("/store/like-list")
    public String likeList(@CurrentUser Member member, Model model) {
        List <Item> likeList = likeService.getLikeList(member);

        model.addAttribute("likeList", likeList);

        return "/view/like-list";
    }
}
