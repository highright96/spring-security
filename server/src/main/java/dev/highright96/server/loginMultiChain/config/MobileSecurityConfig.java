package dev.highright96.server.loginMultiChain.config;

import dev.highright96.server.loginMultiChain.student.StudentManager;
import dev.highright96.server.loginMultiChain.teacher.TeacherManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(1)
@Configuration
@RequiredArgsConstructor
public class MobileSecurityConfig extends WebSecurityConfigurerAdapter {

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

        http
                .antMatcher("/api/**")
                .csrf().disable()
                .authorizeRequests(
                        (requests) ->
                                requests.anyRequest().authenticated())

                .httpBasic();
    }
}
