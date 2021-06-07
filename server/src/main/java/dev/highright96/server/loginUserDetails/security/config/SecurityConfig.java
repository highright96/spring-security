package dev.highright96.server.loginUserDetails.security.config;

import dev.highright96.server.loginUserDetails.security.local.LoginSuccessHandler;
import dev.highright96.server.loginUserDetails.security.local.RestAuthenticationEntryPoint;
import dev.highright96.server.loginUserDetails.security.filter.LoginFilter;
import dev.highright96.server.loginUserDetails.security.filter.TokenAuthenticationFilter;
import dev.highright96.server.loginUserDetails.security.jwt.JwtTokenProvider;
import dev.highright96.server.loginUserDetails.security.local.CustomUserDetailsService;
import dev.highright96.server.loginUserDetails.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    private final CustomUserDetailsService customUserDetailsService;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final JwtTokenProvider jwtTokenProvider;

    private final CorsFilter corsFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .headers().frameOptions().disable().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()

                .addFilter(corsFilter)
                .addFilter(loginFilter())
                .addFilter(new TokenAuthenticationFilter(authenticationManager(), jwtTokenProvider, customUserDetailsService))

                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/login",
                        "/login-form")
                .permitAll()
                .antMatchers("/main").hasAnyRole(USER, ADMIN)
                .antMatchers("/user-page").hasRole(USER)
                .antMatchers("/admin-page").hasRole(ADMIN)
                .anyRequest().permitAll()
                .and()

                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login-form")
                .invalidateHttpSession(true)
                .deleteCookies("Authorization")
                .and()

                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }

    private LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager());
        //loginFilter.setFilterProcessesUrl("/api/user/login"); REST API 일 때 사용
        loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(jwtTokenProvider));
        return loginFilter;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
