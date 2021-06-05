package dev.highright96.server.loginUserDetails.controller;

import dev.highright96.server.loginUserDetails.repository.DbInit;
import dev.highright96.server.loginUserDetails.exception.YouCannotAccessUserPage;
import dev.highright96.server.loginUserDetails.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final DbInit dbInit;

    @PostConstruct
    public void dbInit() throws Exception {
        dbInit.dbInit();
    }

    @GetMapping("/")
    public String main(HttpSession httpSession, Model model) {
        model.addAttribute("sessionId", httpSession.getId());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/login-required")
    public String loginRequired() {
        return "loginRequired";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "loginForm";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "AccessDenied";
    }

    @GetMapping("/access-denied2")
    public String accessDenied2() {
        return "AccessDenied2";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/user-page")
    public String userPage() throws YouCannotAccessUserPage {
        if (true) throw new YouCannotAccessUserPage();
        return "UserPage";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/admin-page")
    public String adminPage() {
        return "AdminPage";
    }

    @ResponseBody
    @GetMapping("/auth")
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
