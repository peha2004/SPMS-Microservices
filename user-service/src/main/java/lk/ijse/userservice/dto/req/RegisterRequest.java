package lk.ijse.userservice.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lk.ijse.userservice.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank private String name;
    @Email @NotBlank private String email;
    @NotBlank private String password;
    private Role role;
}