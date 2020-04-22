package com.example.CustomerContracts.resources;
import com.example.CustomerContracts.services.CustomerAccounts;
import com.example.CustomerContracts.services.CustomerContractService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
public class CustomerContractResource  {
    @Autowired
    private CustomerContractService service;
    @Autowired
    private CustomerAccounts serviceAccounts;

    @GetMapping("/getCustomerContracts")
    public List<String> getCustomerContracts()  throws ExecutionException, InterruptedException {
        return service.getCustomerContracts().get();
    }

    @GetMapping("/getCustomerAcounts")
    public List<String> getCustomerAcounts()  throws ExecutionException, InterruptedException {
        return serviceAccounts.getCustomerAccounts().get();
    }

    @PutMapping("/waitTime/{newWaitTime}")
    public Integer replaceWaitTime(@PathVariable("newWaitTime") Integer newWaitTime)   throws ExecutionException, InterruptedException {
        return service.replaceWaitTime(newWaitTime).get();
    }

    @GetMapping("/waitTime")
    public Integer getWaitTime()  throws ExecutionException, InterruptedException{
        Integer waitTime = service.getWaitTime().get();
        return waitTime;
    }
}
