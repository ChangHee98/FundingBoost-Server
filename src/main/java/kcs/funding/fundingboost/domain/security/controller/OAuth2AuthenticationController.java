package kcs.funding.fundingboost.domain.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1")
public class OAuth2AuthenticationController {


    /**
     * test 용 login Form
     */
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

}
