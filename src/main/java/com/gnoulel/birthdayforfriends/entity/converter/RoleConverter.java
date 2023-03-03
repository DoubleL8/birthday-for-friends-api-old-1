package com.gnoulel.birthdayforfriends.entity.converter;

import com.gnoulel.birthdayforfriends.enums.RoleEnum;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<RoleEnum, String> {
    @Override
    public String convertToDatabaseColumn(RoleEnum role) {
        if (Objects.isNull(role)) return null;
        return role.name();
    }

    @Override
    public RoleEnum convertToEntityAttribute(String name) {
        if (StringUtils.isBlank(name)) return null;
        return RoleEnum.valueOf(name);
    }
}
