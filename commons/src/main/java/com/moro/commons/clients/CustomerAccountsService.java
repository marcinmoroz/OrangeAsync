package com.moro.test.commons.clients;

import com.moro.test.commons.models.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@FeignClient(name = "${CustomerAccounts.feign.name}",  url = "${CustomerAccounts.feign.url}")
public interface CustomerAccountsService {
    @RequestMapping(method = GET, value = "/accounts/getCustomerAccountNumber")
    String getCustomerAccountNumber();
    @RequestMapping(method = GET, value = "/accounts/getCustomerAcounts")
    List<Account> getCustomerAccounts();
}
