package lk.ijse.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lk.ijse.userservice.entity.Role;

@Data
public class RegisterRequest {
    @NotBlank
    private String name;
    @Email
    @NotBlank private String email;
    @NotBlank private String password;
    private Role role;
}
