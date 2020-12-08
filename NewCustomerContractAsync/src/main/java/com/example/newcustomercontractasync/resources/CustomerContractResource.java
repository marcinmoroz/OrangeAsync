package com.example.newcustomercontractasync.resources;

import com.example.newcustomercontractasync.services.CustomerAccounts;
import com.example.newcustomercontractasync.services.CustomerContractService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
public class CustomerContractResource {

    @Autowired
    private CustomerContractService service;
    @Autowired
    private CustomerAccounts serviceAccounts;

    @GetMapping("/getCustomerContracts")
    public CompletableFuture<List<String>> getCustomerContracts() throws InterruptedException {
        return service.getCustomerContracts();
    }

    @GetMapping("/getCustomerAcounts")
    public CompletableFuture<List<String>> getCustomerAcounts() throws InterruptedException {
        return serviceAccounts.getCustomerAccounts();
    }

    @GetMapping("/getCustomerAcount")
    public CompletableFuture<String> getCustomerAcount() throws InterruptedException {
        return serviceAccounts.getCustomerAccount();
    }

    @PutMapping("/waitTime/{newWaitTime}")
    public Integer replaceWaitTime(@PathVariable("newWaitTime") Integer newWaitTime) throws ExecutionException, InterruptedException {
        return service.replaceWaitTime(newWaitTime).get();
    }

    @GetMapping("/waitTime")
    public Integer getWaitTime() throws ExecutionException, InterruptedException {
        Integer waitTime = service.getWaitTime().get();
        return waitTime;
    }
}
