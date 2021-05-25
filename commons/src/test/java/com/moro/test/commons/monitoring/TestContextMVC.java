package com.moro.test.commons.monitoring;


import com.moro.commons.context.monitoring.PrometheusTagsProvider;
import com.moro.commons.monitoring.prometheus.OccPrometheusConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;


@ImportAutoConfiguration({ MetricsAutoConfiguration.class,
        WebMvcAutoConfiguration.class, OccPrometheusConfiguration.class})
@SpringBootApplication
public class TestContextMVC  {
    @Bean
    public PrometheusTagsProvider tagsProvider() {
        return new TestPrometheusTagsProvider();
    }

    public static void main(String[] args) {
        SpringApplication.run(TestContextMVC.class, args);
    }
}
