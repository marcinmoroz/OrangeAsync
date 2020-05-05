package com.example.CustomerContracts.services;

import lombok.var;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@Async("contractsExecutor")
public class CustomerContractService {
  private Integer waitTimeMilliseconds = 1000;

  public CompletableFuture<List<String>> getCustomerContracts() throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(waitTimeMilliseconds);
    var contracts = new ArrayList<String>() {{ add("Contract1");add("Contract2");add("Contract3");}};
    return CompletableFuture.completedFuture(contracts);
  }

  public CompletableFuture<Integer> replaceWaitTime(@PathVariable("newWaitTime") Integer newWaitTime) {
    waitTimeMilliseconds = newWaitTime;
    return CompletableFuture.completedFuture(newWaitTime);
  }

  public CompletableFuture<Integer> getWaitTime() {
    return CompletableFuture.completedFuture(waitTimeMilliseconds);
  }
}
