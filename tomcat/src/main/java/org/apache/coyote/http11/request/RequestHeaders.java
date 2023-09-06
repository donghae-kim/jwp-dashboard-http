package org.apache.coyote.http11.request;

import org.apache.coyote.http11.HttpCookie;

import java.io.BufferedReader;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestHeaders {

    private static final String KEY_VALUE_DELIMITER = ": ";
    private final Map<String, String> headers;

    public RequestHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public static RequestHeaders from(final BufferedReader bufferedReader) {
        final Map<String, String> headers = bufferedReader.lines().takeWhile(it -> !it.isEmpty())
                .distinct()
                .map(it -> it.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(it -> it[0], it -> it[1]));

        return new RequestHeaders(headers);
    }

    public String getValue(String key) {
        return headers.get(key);
    }

    public HttpCookie getCookie(){
        return HttpCookie.parseCookie(headers.get("Cookie"));
    }
}
