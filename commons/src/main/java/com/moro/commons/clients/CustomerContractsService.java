package com.moro.test.commons.clients;

import com.moro.test.commons.models.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@FeignClient(name = "${CustomerContracts.feign.name}",  url = "${CustomerContracts.feign.url}")
public interface CustomerContractsService {
    @RequestMapping(method = GET, value = "/contracts/getCustomerContracts")
    List<String> getCustomerContracts() ;
}
