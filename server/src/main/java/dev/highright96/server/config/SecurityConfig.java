package dev.highright96.server.config;

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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 로 접근을 제어하겠다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthDetails customAuthDetails;

    /*
    임의의 아이디, 비밀번호를 생성한다.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        User.builder()
                                .username("user")
                                .password(passwordEncoder().encode("1111"))
                                .roles("USER")
                )
                .withUser(
                        User.builder()
                                .username("admin")
                                .password(passwordEncoder().encode("2222"))
                                .roles("ADMIN")
                );
    }

    /*
    Spring Security 가 제공하는 암호화
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    Admin 은 User 페이지에 접근 가능
     */
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    /*
    Spring Security 는 모든 페이지를 막는다.
    특정 페이지는 모두 접근할 수 있게 만들어 준다.
    하나의 filter chain 이다.
    */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //http.antMatcher("/**"); // 모든 url 요청은 이 필터 체인을 통과한다.

        http.authorizeRequests(
                (requests) -> requests
                        .antMatchers("/").permitAll()
                        .anyRequest().authenticated())

                .formLogin(
                        login ->
                                login
                                        .loginPage("/login")
                                        .permitAll()
                                        .defaultSuccessUrl("/abc", false)
                                        .failureUrl("/login-error")
                                        .authenticationDetailsSource(customAuthDetails)
                )
                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                )
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied")
                );
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // resource 는 시큐리티 필터를 타지 않음.
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }
}
