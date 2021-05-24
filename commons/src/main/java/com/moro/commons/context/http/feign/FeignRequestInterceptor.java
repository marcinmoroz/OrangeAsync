package com.moro.commons.context.http.feign;

import com.moro.commons.context.http.ApplicationContextThroughHeaders;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Passes
 */
@RequiredArgsConstructor
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    final List<ApplicationContextThroughHeaders> httpContexts;
    @Override
    public void apply(RequestTemplate requestTemplate) {
        httpContexts.forEach( ct -> {
            ct.getContextHeaders().forEach( (headerName, headerValue) -> {
                if(!StringUtils.isEmpty(headerValue))
                    requestTemplate.header(headerName, headerValue);
            });
        });
    }
}
