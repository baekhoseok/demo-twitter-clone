package me.hoseok.twitterdemo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingInterceptor extends HandlerInterceptorAdapter {
    private final ObjectMapper objectMapper;

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {


//        final ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);
//        final ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper(response);
//
//
//        log.info(
//                "ReqBody : {} / ResBody : {}",
//                objectMapper.readTree(cachingRequest.getContentAsByteArray()),
//                objectMapper.readTree(cachingResponse.getContentAsByteArray())
//        );

    }
}
