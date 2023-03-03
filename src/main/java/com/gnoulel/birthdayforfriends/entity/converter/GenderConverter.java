package com.gnoulel.birthdayforfriends.entity.converter;

import com.gnoulel.birthdayforfriends.enums.GenderEnum;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<GenderEnum, String> {
    @Override
    public String convertToDatabaseColumn(GenderEnum gender) {
        if (Objects.isNull(gender)) return null;

        return gender.getCode();
    }

    @Override
    public GenderEnum convertToEntityAttribute(String code) {
        if (StringUtils.isBlank(code)) return null;

        return GenderEnum.of(code);
    }
}
