package com.artSoft.bankChecks.model.user.dto.request;

import com.artSoft.bankChecks.model.user.enums.Role;
import com.artSoft.bankChecks.model.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchRequest {

    private String name;
    private String email;
    private String phone;
    private Role role;
    private UserStatus status;
}
