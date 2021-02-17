package com.moro.test.feignclienttest;

import com.moro.test.commons.clients.CustomerContractsService;
import com.moro.test.commons.models.Account;
import com.moro.test.feignclienttest.tools.OccFeignLogger;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import com.moro.test.commons.clients.CustomerAccountsService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@EnableFeignClients("com.moro.test.commons.clients")
@SpringBootApplication
@Log4j2
public class FeignClientTestApplication  implements CommandLineRunner {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Configuration
    class FeignConfiguration {
        @Bean
        Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }

        @Bean
        Logger feignLogger() {
            return new OccFeignLogger();
        }

        @Bean
        public RequestInterceptor requestInterceptor() {
            return requestTemplate -> {
                requestTemplate.header("systemName", "feignClient");
                requestTemplate.header("time", Instant.now().toString());
            };
        }
    }


    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CustomerAccountsService customerClient;
    @Autowired
    CustomerContractsService customerContractsService;
    @Autowired
    private Tracer tracer;

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
        while(true) {
            Span span = tracer.buildSpan("localSpan")
                    .start();
            tracer.activateSpan(span);
            span.log("Start");
            span.setTag("NotGlobalID", UUID.randomUUID().toString());
            span.setBaggageItem("GlobalId", UUID.randomUUID().toString());
            try {
                log.info("Baggage items");
                span.context().baggageItems().forEach(bI -> log.info(bI.getKey() + "||" + bI.getValue()));
                List<Account> accounts = restTemplate.exchange("http://localhost:8094/CustomerAccounts/accounts/getCustomerAcounts", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Account>>() {
                        }).getBody();
                accounts.forEach(a -> log.info("Account : " + a.toString()));
                String response = customerClient.getCustomerAccountNumber();
                log.info("Response :{}", response);
                accounts = customerClient.getCustomerAccounts();
                accounts.forEach(a -> log.info("Account : " + a.toString()));
                var contracts = customerContractsService.getCustomerContracts();
            } catch (Exception e) {
                log.error(e);
            } finally {
                span.finish();
            }
            Thread.sleep(1000);
        }
    }
}
