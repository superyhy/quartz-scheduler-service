package com.keeson.quartzschedulerservice.infrastructure.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author Yhy
 * @create 2025/10/29 11:17
 * @describe
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();

        // 登录接口放行
        if (uri.startsWith("/api/login") || uri.endsWith("login.html")) {
            return true;
        }

        // 检查 session
        Object user = session.getAttribute("user");
        if (user != null) {
            return true;
        }

        // 未登录，重定向到 login.html
        response.sendRedirect("/login.html");
        return false;
    }
}
