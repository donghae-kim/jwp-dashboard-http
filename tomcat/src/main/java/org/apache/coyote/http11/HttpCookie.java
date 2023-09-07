package org.apache.coyote.http11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class HttpCookie {
    private static final String COOKIES_DELIMITER = "; ";
    private static final String COOKIE_DELIMITER = "=";
    private final Map<String, String> cookies;

    private HttpCookie(final Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public static HttpCookie parseCookie(final String cookie) {
        Map<String,String> parsedCookie = new HashMap<>();
        if(cookie!=null) {
            parsedCookie = Arrays.stream(cookie.split(COOKIES_DELIMITER))
                    .takeWhile(it -> !it.isEmpty())
                    .map(it -> it.split(COOKIE_DELIMITER))
                    .collect(Collectors.toMap(it -> it[0], it -> it[1]));
        }

        return new HttpCookie(parsedCookie);
    }

    public String makeCookieValue(final UUID uuid){
        return "JSESSIONID" + COOKIE_DELIMITER + uuid;
    }

    public boolean checkIdInCookie(){
        return cookies.containsKey("JSESSIONID");
    }

    public String getValue(final String key){
        return cookies.get(key);
    }
}
