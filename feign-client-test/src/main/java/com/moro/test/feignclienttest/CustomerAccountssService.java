package com.moro.test.feignclienttest;

import com.moro.test.commons.models.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;


@FeignClient(name = "${CustomerContractsService.feign.name}",  url = "${CustomerContractsService.feign.url}")
public interface CustomerAccountssService {
    @RequestMapping(method = GET, value = "/accounts/getCustomerAccountNumber")
    String getCustomerAccountNumber();
    @RequestMapping(method = GET, value = "/accounts/getCustomerAcounts")
    List<Account> getCustomerAccounts();
}
