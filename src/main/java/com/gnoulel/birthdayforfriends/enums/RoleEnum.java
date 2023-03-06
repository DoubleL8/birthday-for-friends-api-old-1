package com.gnoulel.birthdayforfriends.enums;

import org.apache.commons.lang3.StringUtils;

public enum RoleEnum {
    USER_ROLE,
    ADMIN_ROLE;

    public RoleEnum of(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }

        return RoleEnum.valueOf(name);
    }

}
