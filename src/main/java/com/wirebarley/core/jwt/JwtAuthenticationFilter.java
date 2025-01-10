package com.wirebarley.core.jwt;

import com.wirebarley.bank.common.dto.LoginVO;
import com.wirebarley.bank.common.utils.BankUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean verificationFlag = true;

        String token = BankUtils.isNull( request.getHeader("Authorization") );

        try {
            if( !StringUtils.hasText( token ) ) {
                verificationFlag = false;
            } else {
                String userId = jwtUtil.getInfoFromToken("userId", token);

                if( !StringUtils.hasText( userId ) ) {
                    verificationFlag = false;
                }

            }
        } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException |
                 SignatureException e) {
            logger.debug("Unable to verify JWT Token: " + e.getMessage());
            verificationFlag = false;
        }

        LoginVO loginVO = null;

        if( verificationFlag ) {
            loginVO = LoginVO.builder()
                    .userId(jwtUtil.getInfoFromToken("userId", token))
                    .name(jwtUtil.getInfoFromToken("name", token))
                    .address(jwtUtil.getInfoFromToken("address", token))
                    .telNo(jwtUtil.getInfoFromToken("telNo", token))
                    .brithDate(jwtUtil.getInfoFromToken("brithDate", token))
                    .useStatus(jwtUtil.getInfoFromToken("useStatus", token))
                    .authorizationCode(jwtUtil.getInfoFromToken("authorizationCode", token))
                    .build();

            String role = "ROLE_"+loginVO.getAuthorizationCode();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( loginVO, null, Arrays.asList( new SimpleGrantedAuthority( role ) ) );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}
