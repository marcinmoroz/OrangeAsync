package pl.orange.opl.newcustomeraccountasync;


import feign.Capability;
import feign.Logger;
import feign.RequestInterceptor;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsContributor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Executor;

@Import(FeignClientsConfiguration.class)
@EnableFeignClients("com.moro.commons.clients")
@SpringBootApplication
public class NewcustomeraccountasyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewcustomeraccountasyncApplication.class, args);
    }

    @Configuration
    class PrometheusConfiguration {
        private final Environment environment;

        public PrometheusConfiguration(Environment environment) {
            this.environment = environment;
        }

        @Bean
        MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
            return registry -> {
              registry.config().commonTags("systemName", environment.getProperty("spring.application.name"));
              registry.config().commonTags("instanceName", "instance1") ;
            };
        }

        @Bean
        public WebMvcTagsContributor webMvcTagsContributor() {
            return new WebMvcTagsContributor() {
                @Override
                public Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response, Object handler, Throwable exception) {
                    return Tags.of(Tag.of("opl-httpStatus", String.valueOf(response.getStatus())));
                }

                @Override
                public Iterable<Tag> getLongRequestTags(HttpServletRequest request, Object handler) {
                    return new ArrayList<>();
                }
            };
        }
    }

    @Configuration
    class FeignConfiguration {
        private final MeterRegistry meterRegistry;
        @Bean
        Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }

        public FeignConfiguration(MeterRegistry meterRegistry) {
            this.meterRegistry = meterRegistry;
        }

        @Bean
        public RequestInterceptor requestInterceptor() {
            return requestTemplate -> {
                requestTemplate.header("systemName", "feignClient");
                requestTemplate.header("time", Instant.now().toString());
            };
        }

        @Bean
        public Capability capability() {
            return new MicrometerCapability(meterRegistry);
        }


//        @Bean
//        public Client client() {
//            Client client = new Client.Default((SSLSocketFactory)null, (HostnameVerifier)null);
//            return new MeteredClient(client, meterRegistry);
//        }
//
//        @Bean
//        public Encoder encoder(ObjectFactory<HttpMessageConverters> messageConverters) {
//            return new MeteredEncoder(new SpringEncoder(messageConverters), meterRegistry);
//        }
//
//        @Bean
//        public Decoder decoder(ObjectFactory<HttpMessageConverters> messageConverters) {
//            return new MeteredDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)), meterRegistry);
//        }
////
//        @Bean
//        public InvocationHandlerFactory invocationHandlerFactory() {
//            return new MeteredInvocationHandleFactory(new InvocationHandlerFactory.Default(), meterRegistry);
//        }
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
