package com.chadev.xcape.core.util;

import java.util.function.Supplier;

public class XcapeUtil {

    public static final Supplier<String> getAuthenticationNumber = () -> {
        StringBuilder random = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            random.append((int) (Math.random() * 10));
        }
        return random.toString();
    };
}
