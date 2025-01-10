package com.wirebarley.core.jwt;


import com.wirebarley.bank.common.entity.Member;
import com.wirebarley.core.cmm.BankProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil implements Serializable {

    private static final long serialVersionUID = -5180902194184255251L;

    public static final long JWT_TOKEN_VALIDITY = (long) ((1 * 60 * 60) / 60) * 60; //토큰의 유효시간 설정, 기본 60분

    public static final String SECRET_KEY = BankProperties.getProperty("bank.jwt.secret");

    public String generateToken( Member member ) {
        return generateToken(member, "Authorization");
    }

    public String generateToken( Member member, String subject ) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", member.getUserId());
        claims.put("name", member.getName());
        claims.put("address", member.getAddress());
        claims.put("telNo", member.getTelNo());
        claims.put("brithDate", member.getBrithDate());
        claims.put("useStatus", member.getUseStatus());
        claims.put("authorizationCode", member.getAuthorizationCode());
        claims.put("type", subject);

        log.debug("===>>> secret = " + SECRET_KEY);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000 * 365 ))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String getInfoFromToken(String type, String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get(type).toString();
    }

    public Claims getAllClaimsFromToken(String token) {
        log.debug("===>>> secret = " + SECRET_KEY);
        if( token.startsWith("Bearer") ) token = token.replace("Bearer ","");
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, String userId) {
        String extractedUserId = getInfoFromToken("userId", token);
        return extractedUserId.equals(userId) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }


}
