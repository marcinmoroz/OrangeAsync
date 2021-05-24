package com.moro.test.commons.logging.feign;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.moro.commons.context.http.ApplicationContextThroughHeaders;
import com.moro.commons.context.http.feign.FeignRequestInterceptor;
import com.moro.commons.logging.OccFeignLogger;
import com.moro.test.commons.logging.TestContextThroughHeaders;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    ApplicationContextThroughHeaders testContext() {return new TestContextThroughHeaders();}

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor(Stream.of(testContext()).collect(Collectors.toList()));
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
