package com.gnoulel.birthdayforfriends.model.converter;

import com.gnoulel.birthdayforfriends.enums.GenderEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenderConverter implements AttributeConverter<GenderEnum, String> {
    @Override
    public String convertToDatabaseColumn(GenderEnum gender) {
        if (gender == null) return null;

        return gender.getCode();
    }

    @Override
    public GenderEnum convertToEntityAttribute(String code) {
        if (code == null) return null;

        return GenderEnum.of(code);
    }
}
