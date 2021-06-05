package dev.highright96.server.loginUserDetails.repository;

import dev.highright96.server.loginUserDetails.domain.User;
import dev.highright96.server.loginUserDetails.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final UserService userService;

    public void dbInit() throws Exception {
        if (!userService.findUser("user1").isPresent()) {
            User user = userService.save(User.builder()
                    .email("user1")
                    .password("1111")
                    .enabled(true)
                    .build());
            userService.addAuthority(user.getUserId(), "ROLE_USER");
        }
        if (!userService.findUser("user2").isPresent()) {
            User user = userService.save(User.builder()
                    .email("user2")
                    .password("1111")
                    .enabled(true)
                    .build());
            userService.addAuthority(user.getUserId(), "ROLE_USER");
        }
        if (!userService.findUser("admin").isPresent()) {
            User user = userService.save(User.builder()
                    .email("admin")
                    .password("1111")
                    .enabled(true)
                    .build());
            userService.addAuthority(user.getUserId(), "ROLE_ADMIN");
        }
    }
}
