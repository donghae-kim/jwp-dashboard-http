package org.apache.coyote.http11.handler;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;
import org.apache.catalina.session.Session;
import org.apache.catalina.session.SessionManager;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.ResourceReader;
import org.apache.coyote.http11.util.Parser;
import org.apache.coyote.http11.exception.NotCorrectPasswordException;
import org.apache.coyote.http11.exception.NotFoundAccountException;
import org.apache.coyote.http11.exception.ResourceNotFoundException;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class LoginHandler{
    private static final String INDEX = "/index.html";
    private static final String LOGIN_PATH = "/login.html";

    private static final String STATIC_PATH = "static";
    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";

    private LoginHandler() {
    }

    private static final Logger log = LoggerFactory.getLogger(LoginHandler.class);

    public static HttpResponse handle(final HttpRequest httpRequest) throws IOException{
        final HttpMethod method = httpRequest.getMethod();

        if (method == HttpMethod.POST) {
            return renderLogin(httpRequest);
        }
        if (method == HttpMethod.GET) {
            if (isLoggedIn(httpRequest)) {
                return HttpResponse.foundResponse(httpRequest, INDEX);
            }
            final String responseBody = ResourceReader.readResource(STATIC_PATH + LOGIN_PATH);
            return HttpResponse.okResponse(httpRequest, responseBody);
        }
        return null;
    }

    private static boolean isLoggedIn(final HttpRequest httpRequest) {
        Session session = findSession(httpRequest);
        return session != null;
    }

    private static Session findSession(final HttpRequest httpRequest) {
        SessionManager sessionManager = new SessionManager();
        String jSessionId = httpRequest.getCookies().getValue("JSESSIONID");

        if (jSessionId == null) {
            return null;
        }
        return sessionManager.findSession(jSessionId);
    }

    private static HttpResponse renderLogin(final HttpRequest httpRequest) {
        final String requestBody = httpRequest.getRequestBody();
        final Map<String, String> queryParams = Parser.queryParamParse(requestBody);

        if (!queryParams.isEmpty()) {
            return login(httpRequest, queryParams);
        }

        return HttpResponse.okResponse(httpRequest, getResource(httpRequest));
    }

    private static String getResource(final HttpRequest httpRequest) {
        final String url = httpRequest.getAbsolutePath();
        try {
            return ResourceReader.readResource(STATIC_PATH + httpRequest.getAbsolutePath());
        } catch (IOException e) {
            throw new ResourceNotFoundException(url);
        }
    }

    private static HttpResponse login(final HttpRequest httpRequest, final Map<String, String> requestParam) {
        final User user = getUser(requestParam);
        log.info(user.toString());
        return HttpResponse.foundResponse(httpRequest, INDEX);
    }

    private static User getUser(final Map<String, String> requestParam) {
        final User user = InMemoryUserRepository.findByAccount(requestParam.get(ACCOUNT))
                .orElseThrow(NotFoundAccountException::new);
        if (!user.checkPassword(requestParam.get(PASSWORD))) {
            throw new NotCorrectPasswordException();
        }
        return user;
    }
}
