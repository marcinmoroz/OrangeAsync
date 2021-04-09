package com.moro.test.commons.logging;

import com.github.tomakehurst.wiremock.WireMockServer;
import feign.Logger;
import feign.RequestInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Instant;

//@SpringBootApplication
@Configuration
@EnableConfigurationProperties
@ImportAutoConfiguration({ FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
@EnableFeignClients(clients = FeignTestClient.class)
public class FeignTestConfig  {
    @Bean
    Logger getLogger() {
        return new OccFeignLogger();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("systemName", "feignClient");
            requestTemplate.header("time", Instant.now().toString());
        };
    }

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockBooksService() {
        return new WireMockServer(9561);
    }
}
