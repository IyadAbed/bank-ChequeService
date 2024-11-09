package com.artSoft.bankChecks.mapper;

import com.artSoft.bankChecks.mapper.assistant.Helper;
import com.artSoft.bankChecks.model.user.documents.User;
import com.artSoft.bankChecks.model.user.dto.request.UserRequest;
import com.artSoft.bankChecks.model.user.dto.response.UserResponse;
import com.artSoft.bankChecks.model.user.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Helper helper;

    public User toEntity(UserRequest request) {
        return User.builder()
                .name(helper.trimString(request.getName()))
                .email(helper.trimString(request.getEmail()))
                .password(passwordEncoder.encode(helper.trimString(request.getPassword())))
                .phone(helper.trimString(request.getPhone()))
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .createdAt(helper.getCurrentDate())
                .updatedAt(helper.getCurrentDate())
                .build();
    }


    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }
}
