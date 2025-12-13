package ua.edu.viti.military.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Витягуємо токен із запиту
            String jwt = getJwtFromRequest(request);

            // 2. Якщо токен є і він валідний
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {

                // 3. Дізнаємося, чий це токен (username)
                String username = jwtUtils.getUsernameFromToken(jwt);

                // 4. Завантажуємо деталі користувача з бази
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Створюємо об'єкт аутентифікації для Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Кажемо Spring Security: "Все ок, це авторизований користувач"
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Не вдалося встановити аутентифікацію користувача: {}", e.getMessage());
        }

        // Пропускаємо запит далі по ланцюжку
        filterChain.doFilter(request, response);
    }

    // Допоміжний метод: дістає текст токена з заголовка "Authorization"
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Перевіряємо, чи починається заголовок зі слова "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Відрізаємо "Bearer " і повертаємо сам токен
        }

        return null;
    }
}