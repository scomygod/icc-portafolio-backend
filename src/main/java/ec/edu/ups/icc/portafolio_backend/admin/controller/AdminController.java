package ec.edu.ups.icc.portafolio_backend.admin.controller;

import ec.edu.ups.icc.portafolio_backend.admin.dto.CreateUserRequest;
import ec.edu.ups.icc.portafolio_backend.admin.dto.UserResponse;
import ec.edu.ups.icc.portafolio_backend.admin.service.AdminService;
import ec.edu.ups.icc.portafolio_backend.user.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;

    public AdminController(AdminService adminService, UserRepository userRepository) {
        this.adminService = adminService;
        this.userRepository = userRepository;
    }

    private Long currentUserId(Authentication auth) {
        return userRepository.findByEmail(auth.getName()).orElseThrow().getId();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public UserResponse createUser(Authentication auth, @RequestBody CreateUserRequest request) {
        return adminService.createUser(currentUserId(auth), request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> listUsers() {
        return adminService.listUsers();
    }
}