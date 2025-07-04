package assessment.manager.controllers;

import assessment.manager.data.models.User;
import assessment.manager.data.repositories.UserRepo;
import assessment.manager.dto.request.RegisterRequest;
import assessment.manager.services.implementations.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class RegisterController {

    private final UserServiceImpl userService;
    private final UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
//        User adminUser = userRepo.findByEmail(adminEmail)
//                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        User registered = userService.register(request);
        return ResponseEntity.status( HttpStatus.CREATED ).body("User registered successfully" );
    }
}

