package com.tkf.teamkimfood.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
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
    private final JwtProperties jwtProperties;

    public JwtTokenProvider(@Value("${jwt.secret_key}")String secretKey, JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key= Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(String subject, Date expiredAt){
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractSubject(String accessToken){
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    public boolean validToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token){
        Claims claims = parseClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(),
                "",authorities),token,authorities);

    }

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

    public Long getUserId(String token){
        Claims claims = parseClaims(token);
        return claims.get("id",Long.class);

    }

//    private Claims getClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(jwtProperties.getSecretKey())
//                .parseClaimsJws(token)
//                .getBody();
//    }


}
