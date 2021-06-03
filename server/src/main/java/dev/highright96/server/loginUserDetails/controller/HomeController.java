package dev.highright96.server.loginUserDetails.controller;

import dev.highright96.server.loginUserDetails.domain.User;
import dev.highright96.server.loginUserDetails.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String main() {
        return "login-basic/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login-basic/loginForm";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login-basic/loginForm";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "login-basic/AccessDenied";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/user-page")
    public String userPage() {
        return "login-basic/UserPage";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin-page")
    public String adminPage() {
        return "login-basic/AdminPage";
    }

    @ResponseBody
    @GetMapping("/auth")
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @ResponseBody
    @GetMapping("/user")
    public User user(String email) {
        return userService.findUser(email).get();
    }
}
