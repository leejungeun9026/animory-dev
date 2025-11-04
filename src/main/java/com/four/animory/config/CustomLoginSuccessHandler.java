package com.four.animory.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
        String from = request.getParameter("from"); // admin/login에서 왔는지

        if ("admin".equals(from) && !isAdmin) {
            // 관리자 로그인 화면에서 일반유저로 로그인 → 경고 후 다시 /admin/login
            response.sendRedirect("/admin/login?notAdmin=1");
            return;
        }

        // 평소 분기
        String redirectUrl = isAdmin ? "/admin/dashboard" : "/";
        response.sendRedirect(redirectUrl);
    }
}