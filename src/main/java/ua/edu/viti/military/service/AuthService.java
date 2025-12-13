package ua.edu.viti.military.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.viti.military.dto.request.LoginRequestDTO;
import ua.edu.viti.military.dto.request.RegisterRequestDTO;
import ua.edu.viti.military.dto.response.JwtResponseDTO;
import ua.edu.viti.military.entity.Role;
import ua.edu.viti.military.entity.RoleName;
import ua.edu.viti.military.entity.User;
import ua.edu.viti.military.repository.RoleRepository;
import ua.edu.viti.military.repository.UserRepository;
import ua.edu.viti.military.security.JwtUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // --- ЛОГІН ---
    public JwtResponseDTO login(LoginRequestDTO loginRequest) {
        // 1. Аутентифікація через Spring Security (перевірка пароля)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // 2. Встановлюємо контекст безпеки
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Генеруємо токен
        String jwt = jwtUtils.generateToken(authentication);

        // 4. Отримуємо деталі користувача для відповіді
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        return new JwtResponseDTO(jwt, user.getUsername(), user.getEmail(), roles);
    }

    // --- РЕЄСТРАЦІЯ ---
    @Transactional
    public String register(RegisterRequestDTO registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Помилка: Такий логін вже зайнятий!");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Помилка: Такий email вже використовується!");
        }

        // Створюємо нового користувача
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Хешуємо пароль!
        user.setFullName(registerRequest.getFullName());
        user.setRank(registerRequest.getRank());

        // Призначаємо роль за замовчуванням (ROLE_USER)
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Помилка: Роль не знайдена."));
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);

        return "Користувач успішно зареєстрований!";
    }
}