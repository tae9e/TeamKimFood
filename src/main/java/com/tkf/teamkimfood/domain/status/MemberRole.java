package com.tkf.teamkimfood.domain.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MemberRole {
    USER("U"),
    ADMIN("A");

    private static final Map<String, MemberRole> CODE_MAP = Stream.of(values())
            .collect(Collectors.toMap(MemberRole::getValue, Function.identity())); // (1)

    @JsonCreator // (2)
    public static MemberRole resolve(String value) {
        return Optional.ofNullable(CODE_MAP.get(value))
                .orElseThrow(() -> new IllegalArgumentException("invalid value"));
    }

    @Converter // (3)
    public static final class MemberRoleConverter implements AttributeConverter<MemberRole, String> {
        @Override
        public String convertToDatabaseColumn(MemberRole attribute) { // (4)

            return attribute.getValue();
        }

        @Override
        public MemberRole convertToEntityAttribute(String dbData) { // (5)

            return MemberRole.resolve(dbData);
        }
    }

    @Getter
    @JsonValue // (6)
    private final String value;

    MemberRole(String value) {
        this.value = value;
    }
}
