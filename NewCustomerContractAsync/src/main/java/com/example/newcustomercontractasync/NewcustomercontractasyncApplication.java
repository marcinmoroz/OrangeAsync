package com.example.newcustomercontractasync;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentracing.Tracer;
import io.opentracing.contrib.mongo.common.TracingCommandListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import com.mongodb.client.MongoClients;

import java.util.concurrent.Executor;

@EnableAsync
@EnableMongoRepositories(basePackages = "com.example.newcustomercontractasync.mongo")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@PropertySource("classpath:/application.yaml")
@Log4j2
public class NewcustomercontractasyncApplication {
    @Autowired
    private Tracer tracer;

    @Bean
    public MicrometerCapability micrometerCapability(MeterRegistry meterRegistry) {
        return new MicrometerCapability(meterRegistry);
    }

    public static void main(String[] args) {
        SpringApplication.run(NewcustomercontractasyncApplication.class, args);
    }

    @Configuration
    public class MongoConfig {
        @Bean
        public MongoClient mongoClient() {
            ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/test");
            // Instantiate TracingCommandListener
            TracingCommandListener listener = new TracingCommandListener.Builder(tracer).build();
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .addCommandListener(listener)
                    .applyConnectionString(connectionString)
                    .build();
            return MongoClients.create(mongoClientSettings);
        }
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
