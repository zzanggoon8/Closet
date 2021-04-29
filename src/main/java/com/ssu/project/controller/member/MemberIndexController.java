package com.ssu.project.controller.member;

import com.ssu.project.domain.member.Member;
import com.ssu.project.domain.member.MemberRepository;
import com.ssu.project.dto.member.MemberSignUpRequestDto;
import com.ssu.project.service.daily.CityNameService;
import com.ssu.project.service.daily.DateService;
import com.ssu.project.service.item.ItemService;
import com.ssu.project.service.member.MemberService;
import com.ssu.project.service.member.MemberSignUpService;
import com.ssu.project.service.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
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

    @GetMapping("/")
    public String index(Model model, String city) {
        model.addAttribute("itemList",itemService.getItemList());

        /////////////////////////////////오늘의 날씨/////////////////////////////////
        String baseDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String meridien = dateService.currentHour();
        if(city != null) {
            String cityName = cityNameService.renameCity(city);
            model.addAttribute("currentTemperature", weatherService.findCurrentDateTemperature(baseDate, cityName, meridien));
            model.addAttribute("weatherList", weatherService.findCurrentLocalWeather(cityName));
            System.out.println("주소가 null 아님");

        }
        else{
            model.addAttribute("currentTemperature", weatherService.findCurrentDateTemperature(baseDate, "서울_인천_경기도", meridien));
            System.out.println("주소가 null일 경우");
        }


        //////////////////////////////////옷 추천/////////////////////////////////////

        //아우터
        Long parent1 = Long.valueOf("12");
        Long child1 = Long.valueOf("29");
        //상의
        Long parent2 = Long.valueOf("3");
        Long child2 = Long.valueOf("2");

        //하의
        Long parent3 = Long.valueOf("31");
        Long child3 = Long.valueOf("36");


        //아우터 가져오기
        model.addAttribute("outer", itemService.findRecommendCategory(parent1, child1));
        // 상의 가져오기
        model.addAttribute("top", itemService.findRecommendCategory(parent2, child2));
        //하의 가져오기
        model.addAttribute("bottom", itemService.findRecommendCategory(parent3, child3));

        return "/view/index";
    }

    // 회원가입
    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute(new MemberSignUpRequestDto());
        return "/view/signup";
    }

    @PostMapping("/signup") // post 요청 시 실행되는 메소드 -> 즉 회원가입 form 작성 시 수행된다.
    public String signUpSubmit(@Valid MemberSignUpRequestDto memberSignUpRequestDto, Errors errors) {

        if (errors.hasErrors()) { // annotation error가 발생 시 error가 담김 -> errors의 error 포함 여부로 판단.
            log.info("validation error occur!");
            return "/view/signup";
        }

        memberSignUpService.validate(memberSignUpRequestDto, errors); // 여기서 이메일 유효성 검증
        log.info("check validation complete!");

        Member member = memberService.createNewMember(memberSignUpRequestDto);

        memberService.autologin(member); // 해당 member를 자동 로그인 하기

        return "redirect:/"; // root로 redirect
    }

    // 로그인
    @PostMapping("/postLogin")
    public String login(@Valid MemberSignUpRequestDto memberSignUpRequestDto, Errors errors) {
        if (errors.hasErrors()) { // annotation error가 발생 시 error가 담김 -> errors의 error 포함 여부로 판단.

            log.info("validation error occur!");
            return "/view/signup";
        }
        Member member = memberRepository.findByEmailAndPassword(memberSignUpRequestDto.getEmail(), memberSignUpRequestDto.getPassword());
        log.info("check validation complete!");
        memberService.autologin(member);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {

        return "/view/login";
    }
}
