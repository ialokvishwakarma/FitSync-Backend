package com.project.FitSync.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${security.jwtKey}")
    private String jwtSecret;

    @Value("${security.expirationTime}")
    private int jwtExpirationTime;

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")) return bearerToken.substring(7);
        return null;
    }

    public String generateTokenFromEmail(String email,String role){
        System.out.println("JWT SECRET USED (generate): " + jwtSecret);
        return Jwts.builder()
                .setSubject(email)
                .claim("roles",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationTime))
                .signWith(key(),SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
