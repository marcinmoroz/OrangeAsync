package com.moro.commons.context.http;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SpringContextInterceptor extends HandlerInterceptorAdapter {
    final List<ApplicationContextThroughHeaders> applicationContextThroughHeaders;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect( Collectors.toMap(Function.identity(), header -> request.getHeader(header)));
        applicationContextThroughHeaders.forEach( c -> c.initializeContextWithHeaders(headers));
        return super.preHandle(request, response, handler);
    }
}
