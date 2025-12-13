package ua.edu.viti.military.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    // --- 1. ГЕНЕРАЦІЯ ТОКЕНА ---
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // Створюємо ключ підпису з нашого секретного рядка
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .subject(userPrincipal.getUsername()) // Записуємо логін у токен
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key) // Підписуємо
                .compact();
    }

    // --- 2. ОТРИМАННЯ ЛОГІНА З ТОКЕНА ---
    public String getUsernameFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // --- 3. ПЕРЕВІРКА ВАЛІДНОСТІ ---
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true; // Все ок, токен справжній і не прострочений

        } catch (MalformedJwtException e) {
            log.error("Некоректний JWT токен: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT токен прострочений: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT токен не підтримується: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT рядок пустий: {}", e.getMessage());
        }

        return false;
    }
}
