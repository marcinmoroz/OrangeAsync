package com.example.demo;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
@Log4j2
public class WebClientTestApplication implements CommandLineRunner {
    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(WebClientTestApplication.class, args);
        log.info("APPLICATION FINISHED");
    }

    private CustomerContractsService customerClient;

    @SneakyThrows
    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
        for (int i = 0; i < args.length; ++i) {
            log.info("args[{}]: {}", i, args[i]);
        }
        String response = customerClient.getCustomerAcount();
        log.info("Response :{}", response);
    }


}
