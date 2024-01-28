package com.tkf.teamkimfood.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Set;


@Component
public class JwtTokenProvider {
    private final Key key;


    public JwtTokenProvider(@Value("${jwt.secret_key}")String secretKey){
        this.key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    //JWT 토큰 생성
    public String generate(String subject, Date expiredAt){
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //토큰에 포함된 사용자 식별 정보
    public String extractSubject(String accessToken){
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    //토큰 유효성 검사
    public boolean validToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Spring Security의 Authenticatioin(인증) 객체 생성
    public Authentication getAuthentication(String token){
        Claims claims = parseClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(),
                "",authorities),token,authorities);

    }

    //토큰에 포함된 Claim(사용자 정보 등) 추출
    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    //JWT 토큰에서 사용자 ID 추출
    public Long getUserId(String token){
        Claims claims = parseClaims(token);
        return claims.get("id",Long.class);

    }


}
