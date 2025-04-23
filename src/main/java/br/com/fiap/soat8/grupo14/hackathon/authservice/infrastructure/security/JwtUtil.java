package br.com.fiap.soat8.grupo14.hackathon.authservice.infrastructure.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.TokenExpiradoException;
import br.com.fiap.soat8.grupo14.hackathon.authservice.application.exceptions.TokenInvalidoException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtil {
	

    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;

    public Date getCurrentDate() {
        return new Date();
    }
    
    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(getCurrentDate())
            .setExpiration(new Date(getCurrentDate().getTime() + expiration))
            .signWith(getSigningKey())
            .compact();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiradoException("Token expirado");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException ex) {
            throw new TokenInvalidoException("Token inválido");
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
      }

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

}
