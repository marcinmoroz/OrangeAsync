package com.example.newcustomeraccountasync;

import com.example.newcustomercontractasync.NewcustomercontractasyncApplication;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@PropertySource("classpath:/application.yaml")
@Log4j2
public class NewcCustomerAccountAsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewcustomercontractasyncApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean(name = "contractsExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(40);
        executor.setQueueCapacity(10);

        executor.setThreadNamePrefix("Contracts-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "accountsExecutor")
    public Executor getaccountsExecutor() {
//		CustomizableThreadFactory factory = new CustomizableThreadFactory("Accounts-");
//		ExecutorService service = Executors.newCachedThreadPool(factory);
//		return service;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(40);
        executor.setQueueCapacity(10);

        executor.setThreadNamePrefix("Accounts-");
        executor.initialize();
        return executor;
    }

}
