package com.gnoulel.birthdayforfriends.utils;

import com.gnoulel.birthdayforfriends.enums.GenderEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class AppUtils {

    /**
     * Get sort direction
     * @param direction String of direction
     * @return Direction
     */
    public static Sort.Direction getDirection(String direction) {
        return Optional.ofNullable(direction)
                .filter(dir -> StringUtils.equals(dir, "asc"))
                .map(dir -> Sort.Direction.ASC)
                .orElse(Sort.Direction.DESC);
    }

    /**
     * Get gender enum of string
     * @param gender - Gender string
     * @return Gender enum
     */
    public static GenderEnum getGender(String gender) {
        return Optional.ofNullable(gender)
                .map(GenderEnum::valueOf)
                .orElseThrow(() -> new IllegalArgumentException("Gender code is invalid!"));
    }
}
