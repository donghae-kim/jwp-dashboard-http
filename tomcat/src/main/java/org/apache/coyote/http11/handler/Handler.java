package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

import java.io.IOException;

public interface Handler {
    HttpResponse handle(final HttpRequest httpRequest) throws IOException;
}
