package org.apache.coyote.http11;

import org.apache.coyote.http11.util.Parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class HttpCookie {
    private static final String JSESSIONID = "JSESSIONID";
    private final Map<String, String> cookies;

    private HttpCookie(final Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public static HttpCookie parseCookie(String cookie) {
        Map<String,String> parsedCookie = new HashMap<>();
        if(cookie!=null) {
            parsedCookie = Parser.cookieParse(cookie);
        }

        return new HttpCookie(parsedCookie);
    }

    public String makeCookieValue(UUID uuid){

        return JSESSIONID + "=" + uuid;
    }

    public boolean checkIdInCookie(){
        return cookies.containsKey(JSESSIONID);
    }

    public String getValue(String key){
        return cookies.get(key);
    }
}
