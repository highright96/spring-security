package dev.highright96.server.loginUserDetails.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // authException 종류에 따라서 인증 만료에 대한 에러 페이지를 보여줄 수 있음.
        request.getRequestDispatcher("/login-required").forward(request, response);
    }
}
