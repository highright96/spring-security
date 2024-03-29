package dev.highright96.server.loginUserDetails.security.local;

import dev.highright96.server.loginUserDetails.domain.User;
import dev.highright96.server.loginUserDetails.repository.UserRepository;
import dev.highright96.server.loginUserDetails.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User findUser = userRepository.findUserByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("유저를 찾을 수 없습니다. email: " + email)
                );
        return UserPrincipal.create(findUser);
    }
}
