package dev.highright96.server.loginUserDetails.repository;

import dev.highright96.server.loginUserDetails.domain.Role;
import dev.highright96.server.loginUserDetails.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void dbInit() throws Exception {
        if (!userRepository.findUserByEmail("user1").isPresent()) {
            User user = userRepository.save(User.builder()
                    .email("user1")
                    .password(passwordEncoder.encode("1111"))
                    .role(Role.ROLE_USER)
                    .build());
        }
        if (!userRepository.findUserByEmail("user2").isPresent()) {
            User user = userRepository.save(User.builder()
                    .email("user2")
                    .password(passwordEncoder.encode("1111"))
                    .role(Role.ROLE_USER)
                    .build());
        }
        if (!userRepository.findUserByEmail("admin").isPresent()) {
            User user = userRepository.save(User.builder()
                    .email("admin")
                    .password(passwordEncoder.encode("1111"))
                    .role(Role.ROLE_ADMIN)
                    .build());
        }
    }
}
