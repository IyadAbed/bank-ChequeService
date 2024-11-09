package com.artSoft.bankChecks.model.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ,
    ADMIN_UPDATE,
    ADMIN_CREATE,
    ADMIN_DELETE
}
