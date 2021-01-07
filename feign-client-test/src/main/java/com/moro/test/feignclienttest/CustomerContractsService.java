package com.moro.test.feignclienttest;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.*;


@FeignClient(name = "${CustomerContractsService.feign.name}",  url = "${CustomerContractsService.feign.url}")
public interface CustomerContractsService {
    @RequestMapping(method = GET, value = "/getCustomerAcount")
    String getCustomerAcount();
}
