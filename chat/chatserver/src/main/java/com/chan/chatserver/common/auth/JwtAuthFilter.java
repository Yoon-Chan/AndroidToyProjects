package com.chan.chatserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends GenericFilter {
    private final HttpServletResponse httpServletResponse;
    @Value("${jwt.secretKey}")
    private String secretKey;

    private final HttpServletRequest httpServletRequest;

    public JwtAuthFilter(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = httpServletRequest.getHeader("Authorization");
        try {
            if (token != null) {
                if (!token.startsWith("Bearer ")) {
                    throw new AuthenticationServiceException("Bearer token invalid");
                }
                Key key = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
                String jwtToken = token.substring(7);
                //토큰 검증 및 claims 추출
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();

                //Authentication 객체 생성
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("invalid token");
        }
    }
}
