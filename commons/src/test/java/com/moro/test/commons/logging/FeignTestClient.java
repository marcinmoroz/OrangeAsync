package com.moro.test.commons.logging;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "test-client", url="${testclient.url}")
public interface FeignTestClient {
    @RequestMapping(value = "hello")
    String hello();
}
