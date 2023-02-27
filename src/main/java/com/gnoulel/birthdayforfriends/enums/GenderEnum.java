package com.gnoulel.birthdayforfriends.enums;

import java.util.stream.Stream;

public enum GenderEnum {
    MALE("M"),
    FEMALE("F");

    private String code;
    GenderEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static GenderEnum of(String code) {
        return Stream.of(GenderEnum.values())
                .filter(gender -> gender.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
