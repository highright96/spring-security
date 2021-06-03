package dev.highright96.server.loginUserDetails.config;

import dev.highright96.server.loginUserDetails.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 로 접근을 제어하겠다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(
                (requests) -> requests
                        .antMatchers("/").permitAll()
                        .anyRequest().authenticated())
                .formLogin(
                        login ->
                                login
                                        .loginPage("/login")
                                        .permitAll()
                                        .defaultSuccessUrl("/", false)
                                        .failureUrl("/login-error")
                )
                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                )
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied")
                );
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }
}
