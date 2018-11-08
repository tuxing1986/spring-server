package io.swagger.security;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * SimpleAuthenticationEntryPoint returns 401 Unauthorized for invalid or missing any credentials.
 */
@Component
public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    /**
     * The 401 JSON response body.
     */
    private static final String ERROR_MESSAGE = "{ \"error\": \"Valid credentials are required to access\" }";

    /**
     * The implementation returns 401 error message in JSON format.
     * 
     * @param request the request
     * @param response the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(ERROR_MESSAGE);
    }
}
