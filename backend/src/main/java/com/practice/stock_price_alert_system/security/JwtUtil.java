package com.practice.stock_price_alert_system.security;

import com.practice.stock_price_alert_system.exception.GlobalException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Key DEVELOPER_GENERATED_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateJwt(UserLoadedFromDbBySecurity userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role",userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
                .signWith(DEVELOPER_GENERATED_SECRET_KEY,SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaimsFromJwt(String jwToken){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(DEVELOPER_GENERATED_SECRET_KEY)
                    .build()
                    .parseClaimsJws(jwToken)
                    .getBody();
        } catch (JwtException e) {
            throw new GlobalException(e.getMessage());
        }
    }

    public String extractUsernameFromJwt(String jwToken){
        return extractClaimsFromJwt(jwToken).getSubject();
    }

    public boolean validatejwToken(String jwToken, UserLoadedFromDbBySecurity userDetails){
        try{
            return extractUsernameFromJwt(jwToken).equals(userDetails.getUsername())
                    &&
                    extractClaimsFromJwt(jwToken).getExpiration().after(Date.from(Instant.now()));
        }catch(ExpiredJwtException e){
            throw new GlobalException("Expired Jwt");
        }catch(JwtException e){
            throw new GlobalException("Invalid Jwt");
        }
    }

}
