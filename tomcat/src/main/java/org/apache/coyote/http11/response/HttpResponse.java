package org.apache.coyote.http11.response;

import org.apache.coyote.http11.HttpContentType;
import org.apache.coyote.http11.HttpCookie;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.request.RequestHeaders;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpResponse {
    private static final String HTTP_PROTOCOL = "HTTP/1.1";

    private final HttpStatus httpStatus;
    private final ResponseHeaders headers;
    private final HttpCookie httpCookie;
    private final String body;

    public HttpResponse(final HttpStatus httpStatus, final ResponseHeaders headers, final HttpCookie httpCookie, final String body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.httpCookie = httpCookie;
        this.body = body;
    }

    public static HttpResponse okResponse(final HttpRequest httpRequest, final String responseBody) {
        final ResponseHeaders responseHeaders = ResponseHeaders.init()
                .addContentType(HttpContentType.valueOfCotentType(httpRequest.getExtension()).getContentType())
                .addContentLength(responseBody);

        return new HttpResponse(HttpStatus.OK, responseHeaders, httpRequest.getCookies(), responseBody);
    }

    public static HttpResponse foundResponse(final HttpRequest httpRequest, final String location) {
        final ResponseHeaders responseHeaders = ResponseHeaders.init()
                .addLocation(location);
        return new HttpResponse(HttpStatus.FOUND, responseHeaders, httpRequest.getCookies(), "");
    }

    public String toResponse() {
        String headersString = headers.getHeaders().entrySet()
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
