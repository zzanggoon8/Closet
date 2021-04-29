package com.ssu.project.controller.email;

import com.ssu.project.domain.email.EmailCheckStatus;
import com.ssu.project.service.member.MemberEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class EmailIndexController {

    private final MemberEmailService memberEmailService;

    @GetMapping("/check-email-token")
    public String checkEmailToken(String email, String token, Model model) {
        EmailCheckStatus status = memberEmailService.processSignUp(email, token);

        switch (status) {
            case WRONG_EMAIL:
                model.addAttribute("error", "wrong email.");
                break;

            case WRONG_TOKEN:
                model.addAttribute("error", "wrong token.");
                break;

            case COMPLETE:
                break;

            case MODIFIED:
                model.addAttribute("error", "already verified account.");
                break;
        }

        model.addAttribute("email", email);
        return "/view/check-email-result";
    }

}
