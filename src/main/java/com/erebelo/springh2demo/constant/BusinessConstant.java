package com.erebelo.springh2demo.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BusinessConstant {

    public static final String HEALTH_CHECK = "/health-check";
    public static final String CUSTOMER = "/customer";
    public static final String PRODUCT = "/product";
    public static final String ORDER = "/order";
    public static final String FILE = "/file";

}