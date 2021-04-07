package com.moro.test.commons.logging;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "test")
public interface FeignTestClient {
    @RequestMapping(value = "hello")
    String hello();
}
