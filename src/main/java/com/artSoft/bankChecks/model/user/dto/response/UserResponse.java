package com.artSoft.bankChecks.model.user.dto.response;

import com.artSoft.bankChecks.model.user.enums.Role;
import com.artSoft.bankChecks.model.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private Role role;

    private UserStatus status;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
