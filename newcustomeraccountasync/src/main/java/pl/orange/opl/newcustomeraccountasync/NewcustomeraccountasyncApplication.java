package pl.orange.opl.newcustomeraccountasync;

import com.moro.test.commons.clients.CustomerContractsService;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.micrometer.*;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.time.Instant;
import java.util.concurrent.Executor;

@Import(FeignClientsConfiguration.class)
@EnableFeignClients("com.moro.test.commons.clients")
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
