package com.erebelo.springh2demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
public enum ErrorEnum {

    ERROR_404_001("%s not found by id: %s"),
    ERROR_404_002("%s not found by id: %s. Try entering it instead of updating it"),
    ERROR_404_003("The delete operation has not been completed as the %s was not found by id: %s"),
    ERROR_404_004("%s not found"),
    ERROR_404_005("%s not found by name: %s"),
    ERROR_409_001("The %s already exists. Try updating it instead of entering it"),
    ERROR_409_002("The %s already exists by name: %s");

    private final String value;

    private static final Map<String, ErrorEnum> ENUM_MAP;

    static {
        Map<String, ErrorEnum> map = new HashMap<>();
        for (ErrorEnum instance : ErrorEnum.values()) {
            map.put(instance.getValue(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static ErrorEnum fromValue(String value) {
        return ENUM_MAP.get(value);
    }
}
