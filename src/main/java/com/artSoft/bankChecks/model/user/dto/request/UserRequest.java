package com.artSoft.bankChecks.model.user.dto.request;

import com.artSoft.bankChecks.model.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$#!%*?&])[a-zA-Z\\d@$#!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one letter, one digit, and one special character")
    private String password;
    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "9715[02456]\\d{7}$", message = "Invalid UAE phone number format.")
    private String phone;
    @NotNull(message = "Role is mandatory")
    private Role role;
}
