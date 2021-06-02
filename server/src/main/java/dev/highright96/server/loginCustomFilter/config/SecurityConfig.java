package dev.highright96.server.loginCustomFilter.config;

import dev.highright96.server.loginCustomFilter.student.StudentManager;
import dev.highright96.server.loginCustomFilter.teacher.TeacherManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 로 접근을 제어하겠다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final StudentManager studentManager;
    private final TeacherManager teacherManager;

    /*
    AuthenticationProvider 등록
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 두 개의 provider 을 모두 돌아 토큰을 받아 인증을 완료한다.
        auth.authenticationProvider(studentManager);
        auth.authenticationProvider(teacherManager);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());

        http.authorizeRequests(
                (requests) -> requests
                        .antMatchers("/", "/login").permitAll()
                        .anyRequest().authenticated())
                .formLogin(
                        login -> login
                                .loginPage("/login").permitAll()
                                .defaultSuccessUrl("/", false)
                                .failureUrl("/login-error")
                )
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class) // UsernamePasswordAuthenticationFilter 를 custom 한 filter 로 대체함.
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"));
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
