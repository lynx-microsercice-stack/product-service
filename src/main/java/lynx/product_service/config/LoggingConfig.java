package lynx.product_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@Configuration
public class LoggingConfig {

    @Bean
    public OncePerRequestFilter requestLoggingFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
                
                // Process the request first
                filterChain.doFilter(requestWrapper, response);
                
                // Log the request after processing
                logRequest(requestWrapper);
            }

            private void logRequest(ContentCachingRequestWrapper request) {
                String requestBody = getRequestBody(request);
                String fullPath = getFullPath(request);
                
                log.info(">>> Incoming request {}, path: {}, body: {}",
                        request.getMethod(),
                        fullPath,
                        requestBody);
            }

            private String getFullPath(HttpServletRequest request) {
                String queryString = request.getQueryString();
                return request.getRequestURI() + (queryString != null ? "?" + queryString : "");
            }

            private String getRequestBody(ContentCachingRequestWrapper request) {
                try {
                    byte[] buf = request.getContentAsByteArray();
                    if (buf.length > 0) {
                        return new String(buf, request.getCharacterEncoding());
                    }
                } catch (UnsupportedEncodingException e) {
                    return "[UNSUPPORTED ENCODING]";
                }
                return "none";
            }
        };
    }
}