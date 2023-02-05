package com.erebelo.springh2demo.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlUtils {

    public static String decodeParam(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
