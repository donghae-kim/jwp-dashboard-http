package org.apache.coyote.http11.request;

import org.apache.coyote.http11.HttpCookie;
import org.apache.coyote.http11.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequest {
    private final RequestURL requestURL;
    private final RequestHeaders requestHeaders;
    private final RequestBody requestBody;
    private static final String CONTENT_LENGTH = "Content-Length";

    private HttpRequest(final RequestURL requestURL, final RequestHeaders requestHeaders, final RequestBody requestBody) {
        this.requestURL = requestURL;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    public static HttpRequest from(final InputStream inputStream) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        final RequestURL requestURL = RequestURL.from(bufferedReader.readLine());
        final RequestHeaders requestHeaders = RequestHeaders.from(bufferedReader);
        final RequestBody requestBody = RequestBody.of(requestHeaders.getValue(CONTENT_LENGTH), bufferedReader);

        return new HttpRequest(requestURL, requestHeaders, requestBody);
    }

    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public HttpCookie getCookies(){
        return requestHeaders.getCookie();
    }

    public String getUrl() {
        return requestURL.getUrl();
    }

    public HttpMethod getMethod() {
        return requestURL.getMethod();
    }

    public String getAbsolutePath() {
        return requestURL.getAbsolutePath();
    }

    public String getExtension(){
        return requestURL.getExtension();
    }

    public String getRequestBody() {
        return requestBody.getRequestBody();
    }
}
