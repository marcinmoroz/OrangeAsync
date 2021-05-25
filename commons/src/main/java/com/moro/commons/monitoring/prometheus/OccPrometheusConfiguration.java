package com.moro.commons.monitoring.prometheus;

import com.moro.commons.context.monitoring.PrometheusTagsProvider;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Configuration
public class OccPrometheusConfiguration {
    private final Environment environment;
    private final List<PrometheusTagsProvider> tagsProviders;

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> {
            if(tagsProviders != null) {
                tagsProviders.forEach( tP -> {
                    tP.getStaticTags().forEach( (k,v) -> registry.config().commonTags(k,v));
                });
            }
//            registry.config().commonTags("systemName", environment.getProperty("spring.application.name"));
//            registry.config().commonTags("instanceName", "instance1") ;
        };
    }

    @Bean
    public WebMvcTagsContributor webMvcTagsContributor() {
        return new WebMvcTagsContributor() {
            @Override
            public Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response, Object handler, Throwable exception) {
                Tags tags = Tags.empty();
                if(tagsProviders != null) {
                    tags = Tags.of(
                    tagsProviders.stream()
                            .flatMap(tP -> tP.getDynamicTags(request, response).entrySet().stream())
                            .map( tag -> Tag.of(tag.getKey(), tag.getValue()))
                            .collect(Collectors.toList())
                    );
                }
                return tags;
            }

            @Override
            public Iterable<Tag> getLongRequestTags(HttpServletRequest request, Object handler) {
                return new ArrayList<>();
            }
        };
    }
}
