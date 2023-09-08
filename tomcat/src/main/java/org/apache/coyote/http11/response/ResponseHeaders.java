package org.apache.coyote.http11.response;

import org.apache.catalina.session.Session;
import org.apache.catalina.session.SessionManager;
import org.apache.coyote.http11.HttpCookie;
import org.apache.coyote.http11.request.HttpRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ResponseHeaders {
    private static Map<String, String> headers;

    public static ResponseHeaders init() {
        headers = new LinkedHashMap<>();
        return new ResponseHeaders();
    }

    public ResponseHeaders addContentType(final String value) {
        this.headers.put("Content-Type", value);
        return this;
    }

    public ResponseHeaders addContentLength(final String body) {
        if (!body.isBlank()) {
            this.headers.put("Content-Length", body.getBytes().length + " ");
        }
        return this;
    }

    public ResponseHeaders addLocation(final String location) {
        this.headers.put("Location", location);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    //    public static ResponseHeaders foundHeader (final String location) {
//        final Map<String, String> responseHeader = new HashMap<>();
//        responseHeader.put("Location", location);
//        return new ResponseHeaders(responseHeader);
//    }
//
//    public static ResponseHeaders loginResponseHeader(final HttpRequest httpRequest) {
//        final Map<String, String> responseHeader = new HashMap<>();
//        setCookie(httpRequest, responseHeader);
//        responseHeader.put("Location", INDEX);
//        return new ResponseHeaders(responseHeader);
//    }
//
//    private static void setCookie(final HttpRequest httpRequest, final Map<String, String> responseHeaders) {
//        final HttpCookie cookie = HttpCookie.parseCookie(httpRequest.getRequestHeaders().getValue("Cookie"));
//        if (!cookie.checkIdInCookie()) {
//            final UUID sessionId = UUID.randomUUID();
//            createSession(sessionId.toString());
//            responseHeaders.put("Set-Cookie", cookie.makeCookieValue(sessionId));
//        }
//    }
//
//    private static void createSession(final String sessionId) {
//        Session session = new Session(sessionId);
//        SessionManager sessionManager = new SessionManager();
//        sessionManager.add(session);
//    }
//
//    public Map<String, String> getHeaders() {
//        return headers;
//    }
}
