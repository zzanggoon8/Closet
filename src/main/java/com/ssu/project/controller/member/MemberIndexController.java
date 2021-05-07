package com.ssu.project.controller.member;

import com.google.gson.JsonObject;
import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.member.MemberRepository;
import com.ssu.project.domain.social.Social;
import com.ssu.project.domain.social.SocialRepository;
import com.ssu.project.domain.social.google.GoogleSocial;
import com.ssu.project.domain.social.google.GoogleSocialRepository;
import com.ssu.project.dto.callBack.CallBackRequestDto;
import com.ssu.project.dto.member.MemberSignUpRequestDto;
import com.ssu.project.dto.social.Profile;
import com.ssu.project.dto.social.Token;
import com.ssu.project.dto.social.google.GoogleCallBackRequestDto;
import com.ssu.project.dto.social.google.GoogleProfile;
import com.ssu.project.dto.social.google.GoogleToken;
import com.ssu.project.service.daily.CityNameService;
import com.ssu.project.service.daily.DateService;
import com.ssu.project.service.item.ItemService;
import com.ssu.project.service.member.MemberService;
import com.ssu.project.service.member.MemberSignUpService;
import com.ssu.project.service.weather.WeatherService;

import com.ssu.project.util.GoogleSocialService;
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
    private final GoogleSocialService googleSocialService;
    private final GoogleSocialRepository googleSocialRepository;

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

        // 2. 네이버 아이디로 연동 결과 Callback 정보(redirect_uri=CALLBACK_URL(출력 포맷))
        String redirectUri = URLEncoder.encode("http://localhost:8080/callback");
        String googleRedirecUri = URLEncoder.encode("http://localhost:8080/googleCallback");

        // 1. 네이버 아이디로 연동 URL 생성하기
        String naverUri = "https://nid.naver.com/oauth2.0/authorize" +
                "?response_type=code" +
                "&client_id=tJnDoWnlch9tVLpV5vd7" +
                "&state=STATE_STRING" +
                "&redirect_uri=" + redirectUri;

        String googleUri = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?response_type=code" +
                "&client_id=491929978030-bcubi8usjo62rdm98ttf3oqf7sk010lu.apps.googleusercontent.com" +
                "&scope=openid%20email" +
                "&redirect_uri=" + googleRedirecUri +
                "&state=security_token%3D138r5719ru3e1%26url%3Dhttps%3A%2F%2Foauth2-login-demo.example.com%2FmyHome" +
                "&login_hint=jsmith@example.com" +
                "&nonce=0394852-3190485-2490358" +
                "&hd=example.com";

        model.addAttribute("naverUri", naverUri);
        model.addAttribute("googleUri", googleUri);

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

    @GetMapping("/googleCallback")
        public String googleCallBack(GoogleCallBackRequestDto googleCallBackRequestDto) {
            GoogleProfile googleProfile = null;
            try {
                GoogleToken googleToken = googleSocialService.getGoogleAccessToken(googleCallBackRequestDto.getCode());

                googleProfile = googleSocialService.getGoogleUserProfile(googleToken);

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(googleProfile);

            GoogleSocial googleSocial = googleSocialRepository.findByGoogleSocialId(googleProfile.getId());
        if (googleSocial == null) {
            MemberSignUpRequestDto memberSignUpRequestDto = MemberSignUpRequestDto.builder()
                    .email(googleProfile.getEmail())
                    .password("dddd")
                    .build();
            Member member = memberService.createNewMember(memberSignUpRequestDto);

            GoogleSocial newsocial = GoogleSocial.builder()
                    .googleSocialId(googleProfile.getId())
                    .member(member)
                    .type("구글")
                    .build();
            googleSocialRepository.save(newsocial);
            memberService.autologin(member);
        } else {
            Member member = memberRepository.findByEmail(googleProfile.getEmail());
            memberService.autologin(member);
        }
        return "/view/socialCallback";
    }
}
