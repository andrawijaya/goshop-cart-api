package com.enigma.controller.interceptor;

import com.enigma.exception.UnauthorizedException;
import com.enigma.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyHeaderInterceptor implements HandlerInterceptor {
    JwtUtil jwtUtil;

    public MyHeaderInterceptor(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI().contains("/register/customer") || request.getRequestURI().contains("/register/admin") ||
                request.getRequestURI().contains("/login") || request.getRequestURI().contains("/logout")) {
            return true;
        }
        String token = request.getHeader("Authorization");
        System.out.println("TOKEN " + token);
        if (token == null) throw new UnauthorizedException();
        String[] bearerToken = token.split(" ");
        return jwtUtil.validateJwtToken(bearerToken[1]);
    }
}
