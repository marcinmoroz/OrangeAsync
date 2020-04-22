package com.example.CustomerContractsAsync.services;

import lombok.var;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Async("accountsExecutor")
public class CustomerAccounts {
  private Integer waitTimeMilliseconds = 50;

  public CompletableFuture<List<String>> getCustomerAccounts() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(waitTimeMilliseconds);
    var contracts = new ArrayList<String>() {{ add("Account1");add("Account2");add("Account3");}};
    return CompletableFuture.completedFuture(contracts);
  }
}
