package com.example.newcustomercontractasync.services;

import lombok.var;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Async("accountsExecutor")
@Service
public class CustomerAccounts {
    private Integer waitTimeMilliseconds = 50;


    public CompletableFuture<List<String>> getCustomerAccounts() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(waitTimeMilliseconds);
        var contracts = new ArrayList<String>() {{
            add("Account1");
            add("Account2");
            add("Account3");
        }};
        return CompletableFuture.completedFuture(contracts);
    }

    public CompletableFuture<String> getCustomerAccount() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(waitTimeMilliseconds);
        var contracts = new ArrayList<String>() {{
            add("Account1");
            add("Account2");
            add("Account3");
        }};
        return CompletableFuture.completedFuture(contracts.stream().collect(Collectors.joining()));
    }
}
