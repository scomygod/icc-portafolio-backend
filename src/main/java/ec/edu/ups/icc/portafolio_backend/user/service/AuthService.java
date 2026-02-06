package ec.edu.ups.icc.portafolio_backend.user.service;

import ec.edu.ups.icc.portafolio_backend.user.dto.AuthResponse;
import ec.edu.ups.icc.portafolio_backend.user.dto.LoginRequest;
import ec.edu.ups.icc.portafolio_backend.user.dto.RegisterRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Role;
import ec.edu.ups.icc.portafolio_backend.user.entity.User;
import ec.edu.ups.icc.portafolio_backend.user.repository.UserRepository;
import ec.edu.ups.icc.portafolio_backend.shared.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        var user = userRepository.findByEmail(request.email()).orElseThrow();
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getRole().name());
    }

    public AuthResponse registerFirstAdmin(RegisterRequest request) {
        if (userRepository.countByRole(Role.ADMIN) > 0) {
            throw new RuntimeException("Ya existe un administrador.");
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getRole().name());
    }
}