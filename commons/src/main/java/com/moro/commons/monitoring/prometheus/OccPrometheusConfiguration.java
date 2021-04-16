package com.moro.commons.monitoring.prometheus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsContributor;


@Configuration
public class OccPrometheusConfiguration {
    private final Environment environment;

    public OccPrometheusConfiguration(Environment environment) {
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
