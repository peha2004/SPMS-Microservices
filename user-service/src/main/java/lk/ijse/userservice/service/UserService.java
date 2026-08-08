package lk.ijse.userservice.service;

import lk.ijse.userservice.dto.req.LoginRequest;
import lk.ijse.userservice.dto.req.RegisterRequest;
import lk.ijse.userservice.dto.resp.AuthResponse;
import lk.ijse.userservice.dto.resp.UserResponse;

public interface UserService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserResponse getUser(Long id);
}