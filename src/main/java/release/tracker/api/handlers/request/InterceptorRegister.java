package release.tracker.api.handlers.request;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorRegister implements WebMvcConfigurer {
    private final String[] excludeSwagger = new String[]{"/v3/api-docs**", "/v3/api-docs/**", "/swagger-config.yaml",
            "/swagger-ui.html", "/swagger-ui/**", "/user/**"};


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestHandler()).excludePathPatterns(excludeSwagger);
    }
}
