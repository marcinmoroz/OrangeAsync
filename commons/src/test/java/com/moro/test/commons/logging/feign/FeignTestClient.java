package com.moro.test.commons.logging.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "test-client", url="${testclient.url}")
public interface FeignTestClient {
    @RequestMapping(value = "/test")
    String test();
}
