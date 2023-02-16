package release.tracker.api.handlers.request;

import release.tracker.api.model.api_response.ApiExceptionInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;


public class RequestHandler implements HandlerInterceptor {
    FirebaseAuth _firebaseAuth = FirebaseAuth.getInstance();
    private static final Logger logger = LogManager.getLogger(RequestHandler.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String bearerToken = authorizationHeader.substring(7);
            try {
                var firebaseToken = _firebaseAuth.verifyIdToken(bearerToken);
                if (firebaseToken.getUid() != null)
                    return true;
            } catch (Exception e) {
                setUnauthorizedResponse(response);
                return false;
            }
        }
        setUnauthorizedResponse(response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        StringBuilder infoBuilder = new StringBuilder("Endpoint triggered --->")
                .append(request.getRequestURI())
                .append("| Method type:").append(request.getMethod());
        logger.info(infoBuilder);
    }

    private void setUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        var responseBody = new ObjectMapper().writeValueAsString(new ApiExceptionInfo("Invalid or missing token"));
        response.setContentType("application/json");
        response.setContentLength(responseBody.length());
        response.getOutputStream().write(responseBody.getBytes());
    }
}

