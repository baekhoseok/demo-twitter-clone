package me.hoseok.twitterdemo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Profile("dev")
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomServletWrapperFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);

//        chain.doFilter(wrappingRequest, wrappingResponse);
//
//        wrappingResponse.copyBodyToResponse();

        try {
            chain.doFilter(wrappingRequest, wrappingResponse);
        } finally {

            log.info(
                    "[{}] {} ReqBody : {} / ResBody : {}",
                    wrappingRequest.getMethod(),
                    wrappingRequest.getRequestURI(),
                    objectMapper.readTree(wrappingRequest.getContentAsByteArray()),
                    objectMapper.readTree(wrappingResponse.getContentAsByteArray())
            );


            // Do not forget this line after reading response content or actual response will be empty!
            wrappingResponse.copyBodyToResponse();

            // Write request and response body, headers, timestamps etc. to log files

        }
    }
}
