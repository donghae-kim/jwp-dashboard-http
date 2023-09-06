package org.apache.coyote.http11.response;

import org.apache.catalina.session.Session;
import org.apache.catalina.session.SessionManager;
import org.apache.coyote.http11.HttpContentType;
import org.apache.coyote.http11.HttpCookie;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class HttpResponse {
    private static final String HTTP_PROTOCOL = "HTTP/1.1";
    private final HttpStatus httpStatus;
    private final Map<String,String> headers;
    private final String body;

    public HttpResponse(final HttpStatus httpStatus, final Map<String, String> headers, final String body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public static HttpResponse okResponse(final HttpRequest httpRequest, final String responseBody) {
        final Map<String, String> responseHeaders = new LinkedHashMap<>();

        responseHeaders.put("Content-Type", HttpContentType.valueOfCotentType(httpRequest.getExtension()).getContentType());
        if (!responseBody.isBlank()) {
            responseHeaders.put("Content-Length", responseBody.getBytes().length + " ");
        }

        return new HttpResponse(HttpStatus.OK, responseHeaders, responseBody);
    }

    public static HttpResponse foundResponse(final HttpRequest httpRequest, final String location) {
        final Map<String, String> responseHeaders = new HashMap<>();
        setCookie(httpRequest,responseHeaders);
        responseHeaders.put("Location", location);
        return new HttpResponse(HttpStatus.FOUND, responseHeaders, "");
    }

    private static void setCookie(final HttpRequest httpRequest, final Map<String, String> responseHeaders) {
        final HttpCookie cookie = HttpCookie.parseCookie(httpRequest.getRequestHeaders().getValue("Cookie"));
        if (!cookie.checkIdInCookie()) {
            final UUID sessionId = UUID.randomUUID();
            createSession(sessionId.toString());
            responseHeaders.put("Set-Cookie", cookie.makeCookieValue(sessionId));
        }
    }

    private static void createSession(final String sessionId) {
        Session session = new Session(sessionId);
        SessionManager sessionManager = new SessionManager();
        sessionManager.add(session);
    }

    public String toResponse(){
        String headersString = headers.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\r\n"));

        String responseWithoutBody = String.join("\r\n",
                HTTP_PROTOCOL + " " + httpStatus.getCode() + " " + httpStatus.getMessage(),
                headersString,
                ""
        );

        if (body.isBlank()) {
            return responseWithoutBody;
        }

        return responseWithoutBody + "\r\n" + body;
    }
}
