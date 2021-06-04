package dev.highright96.server.loginUserDetails.config;


import org.springframework.security.access.AccessDeniedException;

public class YouCannotAccessUserPage extends AccessDeniedException {

    public YouCannotAccessUserPage() {
        super("유저페이지 접근 거부");
    }

}
