package dev.highright96.server.loginUserDetails.controller;

import dev.highright96.server.loginUserDetails.domain.User;
import dev.highright96.server.loginUserDetails.domain.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SessionController {

    private final SessionRegistry sessionRegistry;

    /*
    현재 Security 내에 있는 세션들을 보여준다.
     */
    @GetMapping("/sessions")
    public String sessions(Model model) {
        List<UserSession> sessionList =
                sessionRegistry.getAllPrincipals().stream().map(p ->
                        UserSession.builder()
                                .username(((User) p).getUsername())
                                .sessions(sessionRegistry.getAllSessions(p, false).stream().map(s ->
                                        UserSession.SessionInfo.builder()
                                                .sessionId(s.getSessionId())
                                                .time(s.getLastRequest())
                                                .build()).collect(Collectors.toList())
                                ).build()).collect(Collectors.toList());
        model.addAttribute("sessionList", sessionList);
        return "/sessionList";
    }

    /*
    특정 세션을 만료 처리한다.
    */
    @PostMapping("/session/expire")
    public String expireSession(@RequestParam String sessionId) {
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if (!sessionInformation.isExpired()) {
            sessionInformation.expireNow();
        }
        return "redirect:/sessions";
    }

    @GetMapping("/session-expired")
    public String sessionExpired() {
        return "/sessionExpired";
    }
}
