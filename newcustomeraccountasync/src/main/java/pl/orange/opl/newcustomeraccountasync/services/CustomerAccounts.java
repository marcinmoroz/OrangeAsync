package pl.orange.opl.newcustomeraccountasync.services;

import com.moro.test.commons.models.Account;
import com.moro.test.commons.models.Contract;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Async("accountsExecutor")
@Service
@Log4j2
public class CustomerAccounts {
    @Autowired
    private Tracer tracer;
    private Integer waitTimeMilliseconds = 50;

    public CompletableFuture<List<Account>> getCustomerAccounts() throws InterruptedException {
        Span span = tracer.buildSpan("CustomerAccountsService-getCustomerAccounts")
                .start();
        tracer.activateSpan(span);
        span.context().baggageItems().forEach(bI -> log.info("CustomerAccountsService : " +bI.getKey() + "||"+bI.getValue()));

        TimeUnit.MILLISECONDS.sleep(waitTimeMilliseconds);
        Contract contract1 = Contract.builder().id(1).number("Contract1").build();
        Account account1 = Account.builder().id(1).number("Account1").build();
        account1.setContracts( new ArrayList<Contract>(){{add(contract1);}});
        var accounts = new ArrayList<Account>(){{add(account1);}};
        span.finish();
        return CompletableFuture.completedFuture(accounts);
    }

    public CompletableFuture<String> getCustomerAccountsNumber() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(waitTimeMilliseconds);
        var accounts = new ArrayList<String>() {{
            add("Account1");
            add("Account2");
            add("Account3");
        }};
        return CompletableFuture.completedFuture(accounts.stream().collect(Collectors.joining()));
    }

    public CompletableFuture<Integer> replaceWaitTime(@PathVariable("newWaitTime") Integer newWaitTime) {
        waitTimeMilliseconds = newWaitTime;
        return CompletableFuture.completedFuture(newWaitTime);
    }

    public CompletableFuture<Integer> getWaitTime() {
        return CompletableFuture.completedFuture(waitTimeMilliseconds);
    }
}
