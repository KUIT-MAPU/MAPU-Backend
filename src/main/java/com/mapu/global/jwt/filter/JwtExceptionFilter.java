package com.mapu.global.jwt.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mapu.global.common.response.BaseErrorResponse;
import com.mapu.global.common.response.status.ResponseStatus;
import com.mapu.global.jwt.exception.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }catch (JwtException e){
            setErrorResponse(HttpStatus.UNAUTHORIZED, request , response, e);
        }
    }

    public void setErrorResponse(HttpStatus unauthorized, HttpServletRequest request, HttpServletResponse response, JwtException e) throws IOException {
        response.setStatus(unauthorized.value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(
                new BaseErrorResponse(e.getExceptionStatus())
                        .convertToJson()
        );
    }
}
