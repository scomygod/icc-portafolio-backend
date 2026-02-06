package ec.edu.ups.icc.portafolio_backend.admin.service;

import ec.edu.ups.icc.portafolio_backend.admin.dto.CreateUserRequest;
import ec.edu.ups.icc.portafolio_backend.admin.dto.UserResponse;
import ec.edu.ups.icc.portafolio_backend.admin.entity.AdminAudit;
import ec.edu.ups.icc.portafolio_backend.admin.repository.AdminRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Role;
import ec.edu.ups.icc.portafolio_backend.user.entity.User;
import ec.edu.ups.icc.portafolio_backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    public UserResponse createUser(Long adminId, CreateUserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.valueOf(request.role()));
        userRepository.save(user);

        AdminAudit audit = new AdminAudit();
        audit.setAdminId(adminId);
        audit.setAction("CREATE_USER:" + user.getEmail());
        adminRepository.save(audit);

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream()
            .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole().name()))
            .toList();
    }
}