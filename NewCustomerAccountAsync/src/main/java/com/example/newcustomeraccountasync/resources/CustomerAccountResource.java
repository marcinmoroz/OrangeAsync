package com.example.newcustomercontractasync.resources;

import com.moro.test.commons.models.Account;
import com.example.newcustomercontractasync.services.CustomerAccounts;
import com.example.newcustomercontractasync.services.CustomerContractService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@RequestMapping("/accounts")
public class CustomerAccountResource {
    @Autowired
    private CustomerAccounts customerAccounts;

    @GetMapping("/getCustomerAcounts")
    public CompletableFuture<List<Account>> getCustomerAcounts() throws InterruptedException {
        return customerAccounts.getCustomerAccounts();
    }

    @GetMapping("/getCustomerAccountNumber")
    public CompletableFuture<String> getCustomerAccountNumber() throws InterruptedException {
        return customerAccounts.getCustomerAccountsNumber();
    }

    @PutMapping("/waitTime/{newWaitTime}")
    public Integer replaceWaitTime(@PathVariable("newWaitTime") Integer newWaitTime) throws ExecutionException, InterruptedException {
        return customerAccounts.replaceWaitTime(newWaitTime).get();
    }

    @GetMapping("/waitTime")
    public Integer getWaitTime() throws ExecutionException, InterruptedException {
        Integer waitTime = customerAccounts.getWaitTime().get();
        return waitTime;
    }
}
