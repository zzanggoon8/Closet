package com.ssu.project.controller.detail;

import com.ssu.project.domain.item.Item;
import com.ssu.project.domain.member.CurrentUser;
import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.member.MemberRepository;
import com.ssu.project.dto.review.ReviewForm;
import com.ssu.project.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DetailIndexController {

    private final ItemService itemService;
    private final MemberRepository memberRepository;

    @GetMapping("/store/detail/{id}")
    public String detail(@CurrentUser Member member, @PathVariable Long id, Model model) {
        log.info("id : " + id);

        Item item = itemService.findItem(id);
//        List<Review> reviewList = reviewService.findAll(item);

        model.addAttribute(new ReviewForm());
        model.addAttribute("like_status", false);
        if (member != null) {
            member = memberRepository.findByEmail(member.getEmail());
            model.addAttribute("like_status", member.getLikes().contains(item));
        }
        model.addAttribute("item", item);
        model.addAttribute("currentUser",member);
//        model.addAttribute("reviewList",reviewList);

        System.out.println();

        return "/view/detail";
    }

}
