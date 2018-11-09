package io.swagger.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Getter
    @Setter
    private String authorizationHeader;

    @Getter
    @Setter
    private String secret;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getMethod() != null && "OPTIONS".equals(request.getMethod())) {
            return true;
        }
        boolean result = true;
        String token = request.getHeader(authorizationHeader);
        if (token == null || token.isEmpty()) {
            result = false;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Miss auth credentials");
        } else {
            Claims body = Jwts.parser().setSigningKey(secret.getBytes())
                    .parseClaimsJws(token.replace("Bearer", "").trim()).getBody();
            String user = body.getSubject();
            List roles = (List) body.get("roles");
            if (user == null || roles == null) {
                result = false;
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("BE_UNAUTHENTICATED");
            } else {
                // set the user and roles in request
                request.setAttribute("user", user);
                request.setAttribute("roles", roles);
            }
        }
        return result;
    }

}
