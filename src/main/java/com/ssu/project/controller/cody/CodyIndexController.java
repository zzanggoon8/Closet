package com.ssu.project.controller.cody;

import com.google.gson.JsonObject;
import com.ssu.project.domain.cody.Cody;
import com.ssu.project.domain.cody.CodyForm;
import com.ssu.project.domain.item.Item;
import com.ssu.project.domain.member.CurrentUser;
import com.ssu.project.domain.member.Member;
import com.ssu.project.service.cody.CodyService;
import com.ssu.project.service.like.LikeService;
import com.ssu.project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class CodyIndexController {
    private final LikeService likeService;
    private final CodyService codyService;

    @GetMapping("/cody")
    public String cody(@CurrentUser Member member, Model model) {
        model.addAttribute(new CodyForm());

        List<Item> likeList = likeService.getLikeList(member);
        List<Item> top = new ArrayList<>();
        List<Item> outer = new ArrayList<>();
        List<Item> bottom = new ArrayList<>();
        List<Item> acc = new ArrayList<>();
        List<Item> shoes = new ArrayList<>();

        for (int i = 0; i<likeList.size(); i++) {
            if (likeList.get(i).getParentCategory().getName().equals("상의")) {
                top.add(likeList.get(i));
                System.out.println(likeList.get(i).getParentCategory().getName());
            } if (likeList.get(i).getParentCategory().getName().equals("아우터")) {
                outer.add(likeList.get(i));
                System.out.println(likeList.get(i).getParentCategory().getName());
            } if (likeList.get(i).getParentCategory().getName().equals("바지")) {
                bottom.add(likeList.get(i));
                System.out.println(likeList.get(i).getParentCategory().getName());
            } if (likeList.get(i).getParentCategory().getName().equals("신발")||likeList.get(i)
                    .getParentCategory().getName().equals("스니커즈") ) {
                shoes.add(likeList.get(i));
                System.out.println(likeList.get(i).getParentCategory().getName());
            } if(likeList.get(i).getParentCategory().getName().equals("가방")) {
                acc.add(likeList.get(i));
                System.out.println(likeList.get(i).getParentCategory().getName());
            }
        }

        model.addAttribute("topList", top);
        model.addAttribute("outerList", outer);
        model.addAttribute("bottomList", bottom);
        model.addAttribute("accList", acc);
        model.addAttribute("shoesList", shoes);
        model.addAttribute("member", member);

        System.out.println("상의--------->" + top);

        return "/view/cody";
    }

    @PostMapping("/cody")
    public String codySubmit(@CurrentUser Member member, @Valid CodyForm codyForm) {
        Cody cody = codyService.createNewCody(member,codyForm);

        return "redirect:/cody";
    }

    @GetMapping("/codyList")
    public String codyList(@CurrentUser Member member, Model model) {
        model.addAttribute("codyList",codyService.getCodyList(member));

        return "/view/codyList";
    }

    @GetMapping("/allCodyList")
    public String allCodyList(@CurrentUser Member member,Model model) {
        model.addAttribute("codyList",codyService.getAllList());

        model.addAttribute("cody_like_status", false);
//        if (member != null) {
//            member = memberRepository.findByEmail(member.getEmail());
//            model.addAttribute("cody_like_status", member.getCodyLikes().contains(cody));
//        }
        return "/view/allCodyList";
    }

    /**
     * 찜 목록
     * @param member
     * @param codyId
     * @return
     */
    @GetMapping("/cody/like")
    public String addCodyLike(@CurrentUser Member member, @RequestParam("id") Long codyId) {

        boolean result = false;

        JsonObject jsonObject = new JsonObject();

        try {
            result = likeService.addLike(member, codyId);
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
}
