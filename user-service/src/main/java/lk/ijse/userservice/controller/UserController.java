package lk.ijse.userservice.controller;

import jakarta.validation.Valid;
import lk.ijse.userservice.dto.req.LoginRequest;
import lk.ijse.userservice.dto.req.RegisterRequest;
import lk.ijse.userservice.dto.resp.AuthResponse;
import lk.ijse.userservice.dto.resp.UserResponse;
import lk.ijse.userservice.service.UserService;
import lk.ijse.userservice.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Login successful", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUser(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "User retrieved successfully", response));
    }
}