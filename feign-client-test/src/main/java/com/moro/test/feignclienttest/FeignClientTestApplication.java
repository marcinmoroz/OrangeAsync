package com.moro.test.feignclienttest;

import com.moro.test.feignclienttest.tools.OccFeignLogger;
import feign.Feign;
import feign.Logger;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

@EnableFeignClients
@SpringBootApplication
@Log4j2
public class FeignClientTestApplication  implements CommandLineRunner {

//    @Autowired
//    CustomerContractsService customerClient;

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(FeignClientTestApplication.class, args).close();
        log.info("APPLICATION FINISHED");
    }

    @SneakyThrows
    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
        for (int i = 0; i < args.length; ++i) {
            log.info("args[{}]: {}", i, args[i]);
        }
        try {
            CustomerContractsService customerClient = Feign.builder().
                    contract(new SpringMvcContract()).
                    logger(new OccFeignLogger()).
                    logLevel(Logger.Level.FULL).
                    target(CustomerContractsService.class, "http://localhost:8093/CustomerContracts");
            String response = customerClient.getCustomerAcount();
            log.info("Response :{}", response);
        } catch (Exception e) {
            log.error(e);
        }
    }
}
