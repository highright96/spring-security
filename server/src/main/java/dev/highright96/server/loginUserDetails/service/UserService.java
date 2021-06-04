package dev.highright96.server.loginUserDetails.service;

import dev.highright96.server.loginUserDetails.domain.Authority;
import dev.highright96.server.loginUserDetails.domain.User;
import dev.highright96.server.loginUserDetails.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException(username)
        );
    }

    public Optional<User> findUser(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void addAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            Authority newRole = new Authority(user.getUserId(), authority);
            if (user.getAuthorities() == null) {
                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            } else if (!user.getAuthorities().contains(newRole)) {
                HashSet<Authority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }
        });
    }

    public void removeAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            if (user.getAuthorities() == null) return;
            Authority targetRole = new Authority(user.getUserId(), authority);
            if (user.getAuthorities().contains(targetRole)) {
                user.setAuthorities(
                        user.getAuthorities().stream().filter(auth -> !auth.equals(targetRole))
                                .collect(Collectors.toSet())
                );
                save(user);
            }
        });
    }
}
