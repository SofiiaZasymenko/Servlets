package org.example;

import javax.servlet.http.Cookie;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Utils {

    public static Map<String, List<String>> parseQueryString(String query) {
        Map<String, List<String>> result = new HashMap<>();
        if (query == null || query.isBlank()) {
            return result;
        }
        String strippedQuery = query.strip();
        String[] params = (strippedQuery.startsWith("?") ? strippedQuery.substring(1) : strippedQuery)
                .split("&");
        for (String param : params) {
            String[] splitParam = param.split("=");
            List<String> values = result.computeIfAbsent(splitParam[0], key -> new ArrayList<>());
            if (splitParam.length > 1) {
                Arrays.stream(splitParam[1].split(","))
                        .filter(value -> !value.isBlank())
                        .forEach(values::add);
            }
        }
        return result;
    }

    public static boolean isTimezoneValid(String timezone) {
        try {
            ZoneId.of(timezone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Optional<Cookie> findCookieByName(String name, Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .findFirst();
    }
}