package pl.orange.opl.newcustomeraccountasync.resources;

import com.moro.commons.models.Account;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.orange.opl.newcustomeraccountasync.services.CustomerAccounts;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@RequestMapping("/accounts")
public class CustomerAccountResource {
    @Autowired
    private CustomerAccounts customerAccounts;
    @Autowired
    private Tracer tracer;

    @GetMapping("/getCustomerAcounts")
    public CompletableFuture<List<Account>> getCustomerAcounts() throws InterruptedException {

        Span span = tracer.activeSpan();
        log.info("Baggage items");
        span.context().baggageItems().forEach(bI -> log.info(bI.getKey() + "||"+bI.getValue()));
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
