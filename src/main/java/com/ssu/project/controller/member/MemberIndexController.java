package com.ssu.project.controller.member;

import com.google.gson.JsonObject;
import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.member.MemberRepository;
import com.ssu.project.domain.social.Social;
import com.ssu.project.domain.social.SocialRepository;
import com.ssu.project.dto.callBack.CallBackRequestDto;
import com.ssu.project.dto.member.MemberSignUpRequestDto;
import com.ssu.project.dto.social.Profile;
import com.ssu.project.dto.social.Token;
import com.ssu.project.service.daily.CityNameService;
import com.ssu.project.service.daily.DateService;
import com.ssu.project.service.item.ItemService;
import com.ssu.project.service.member.MemberService;
import com.ssu.project.service.member.MemberSignUpService;
import com.ssu.project.service.weather.WeatherService;
import com.ssu.project.util.SocialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberIndexController {
    private final MemberSignUpService memberSignUpService;
    private final MemberService memberService;
    private final ItemService itemService;
    private final DateService dateService;
    private final CityNameService cityNameService;
    private final WeatherService weatherService;
    private final MemberRepository memberRepository;
    private final SocialService socialService;
    private final SocialRepository socialRepository;

    @GetMapping("/")
    public String index(Model model, String city) {
        model.addAttribute("itemList",itemService.getItemList());

        String baseDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String meridien = dateService.currentHour();

        if (city != null) {
            String cityName = cityNameService.renameCity(city);
            model.addAttribute("currentTemperature", weatherService.findCurrentDateTemperature(baseDate, cityName, meridien));
            model.addAttribute("weatherList", weatherService.findCurrentLocalWeather(cityName));
            System.out.println("주소가 null 아님");
        } else {
            model.addAttribute("currentTemperature", weatherService.findCurrentDateTemperature(baseDate, "서울_인천_경기도", meridien));
            System.out.println("주소가 null일 경우");
        }

        Long parent1 = Long.valueOf("12");
        Long child1 = Long.valueOf("29");
        Long parent2 = Long.valueOf("3");
        Long child2 = Long.valueOf("2");
        Long parent3 = Long.valueOf("31");
        Long child3 = Long.valueOf("36");

        model.addAttribute("outer", itemService.findRecommendCategory(parent1, child1));
        model.addAttribute("top", itemService.findRecommendCategory(parent2, child2));
        model.addAttribute("bottom", itemService.findRecommendCategory(parent3, child3));

        return "/view/index";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute(new MemberSignUpRequestDto());
        return "/view/signup";
    }

    @PostMapping("/signup") // post 요청 시 실행되는 메소드 -> 즉 회원가입 form 작성 시 수행된다.
    public String signUpSubmit(@Valid MemberSignUpRequestDto memberSignUpRequestDto, Errors errors) {

        if (errors.hasErrors()) {
            log.info("validation error occur!");
            return "/view/signup";
        }

        memberSignUpService.validate(memberSignUpRequestDto, errors);

        log.info("check validation complete!");

        Member member = memberService.createNewMember(memberSignUpRequestDto);

        memberService.autologin(member);

        return "redirect:/";
    }

    @PostMapping("/postLogin")
    public String login(@Valid MemberSignUpRequestDto memberSignUpRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            log.info("validation error occur!");

            return "/view/signup";
        }

        Member member = memberRepository.findByEmailAndPassword(memberSignUpRequestDto.getEmail(), memberSignUpRequestDto.getPassword());

        log.info("check validation complete!");

        memberService.autologin(member);

        return "redirect:/";
    }


    @GetMapping("/login")
    public String login(Model model) {
        String redirectUri = URLEncoder.encode("http://localhost:8080/callback");

        String naverUri = "https://nid.naver.com/oauth2.0/authorize?response_type=code" +
                "&client_id=tJnDoWnlch9tVLpV5vd7" +
                "&state=fdhtffjygfkg" +
                "&redirect_uri=" + redirectUri;

        model.addAttribute("naverUri", naverUri);

        return "/view/login";
    }

    @GetMapping("/callback")
    public String callback(Model model, CallBackRequestDto callBackRequestDto) {
        Profile profile = null;
        try {
            Token token = socialService.getAccessToken(callBackRequestDto.getCode(), callBackRequestDto.getState());
            profile = socialService.getUserProfile(token);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(profile);

        Social social = socialRepository.findBySocialId(profile.getId());

        if (social == null) {
            MemberSignUpRequestDto memberSignUpRequestDto = MemberSignUpRequestDto.builder()
                    .email(profile.getEmail())
                    .password("dddd")
                    .build();
            Member member = memberService.createNewMember(memberSignUpRequestDto);

            Social newsocial = Social.builder()
                    .socialId(profile.getId())
                    .member(member)
                    .type("네이버")
                    .build();
            socialRepository.save(newsocial);
            memberService.autologin(member);
        } else {
            Member member = memberRepository.findByEmail(profile.getEmail());
            memberService.autologin(member);
        }
        return "/view/socialCallback";
    }
}
