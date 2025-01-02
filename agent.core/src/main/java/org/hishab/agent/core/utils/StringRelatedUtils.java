package org.hishab.agent.core.utils;

import java.util.Optional;

public final class StringRelatedUtils {

    private StringRelatedUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isNullOrEmpty(String str) {
        return Optional.ofNullable(str).map(String::isEmpty).orElse(true);
    }
}
