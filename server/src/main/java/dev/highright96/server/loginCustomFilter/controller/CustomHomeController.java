package dev.highright96.server.loginCustomFilter.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomHomeController {

    @GetMapping("/")
    public String index() {
        return "login-custom-filter/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login-custom-filter/loginForm";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login-custom-filter/loginForm";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "login-custom-filter/AccessDenied";
    }

    @ResponseBody
    @GetMapping("/auth")
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
