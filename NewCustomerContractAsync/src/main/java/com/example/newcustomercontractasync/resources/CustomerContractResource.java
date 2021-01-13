package com.example.newcustomercontractasync.resources;

import com.example.newcustomercontractasync.services.CustomerContractService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@RequestMapping("/contracts")
public class CustomerContractResource {

    @Autowired
    private CustomerContractService service;

    @GetMapping("/getCustomerContracts")
    public CompletableFuture<List<String>> getCustomerContracts() throws InterruptedException {
        return service.getCustomerContracts();
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
