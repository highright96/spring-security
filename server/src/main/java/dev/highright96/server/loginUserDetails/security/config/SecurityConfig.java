package dev.highright96.server.loginUserDetails.security.config;

import dev.highright96.server.loginUserDetails.security.CustomDeniedHandler;
import dev.highright96.server.loginUserDetails.security.CustomEntryPoint;
import dev.highright96.server.loginUserDetails.security.oauth2.CustomOAuth2UserService;
import dev.highright96.server.loginUserDetails.security.local.CustomUserDetailsService;
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
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 로 접근을 제어하겠다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/",
                        "/error",
                        "/favicon.ico",
                        "/sessions",
                        "/session/expire",
                        "/session-expired",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", false)
                .failureUrl("/login-error")
                .and()

                .logout()
                .logoutSuccessUrl("/")
                .and()

                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .and()

                .exceptionHandling()
                //.accessDeniedPage("/access-denied")
                .accessDeniedHandler(new CustomDeniedHandler()) // 접근 권한 핸들러를 직접 생성
                .authenticationEntryPoint(new CustomEntryPoint()) // 인증 실패 페이지 -> 잘못된 세션 정보를 갖고 있는 경우.
                .and()

                .sessionManagement()
                // 세션을 생성시킬지를 설정한다.(JWT 와 같은 세션을 사용하지 않을 경우 STATELESS 로 설정한다.)
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 고정 세션 아이디 사용 x
                .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)
                // 동시 접속을 관리
                .maximumSessions(2) // 최대 몇 개의 세션(동시 접속)을 인정할 것인지.
                .maxSessionsPreventsLogin(false) // 기존 로그인된 세션과 새로 로그인된 세션중 누구를 만료시킬건지? false -> 기존 세션을 만료
                .expiredUrl("/session-expired"); // 세션이 종료되면 보내줄 페이지를 정함
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
        //return new BCryptPasswordEncoder();
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
