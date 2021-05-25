package com.moro.test.commons.monitoring;

import com.moro.commons.context.monitoring.PrometheusTagsProvider;
import com.moro.commons.monitoring.prometheus.OccPrometheusConfiguration;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"management.metrics.web.server.request.autotime.enabled= true"})
@AutoConfigureMockMvc
class MVCMetricTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MeterRegistry registry;
    @MockBean
    TestPrometheusTagsProvider tagsProvider;


    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Order(1)
    public void verifyMetricsAreCountedForApi() throws Exception {
        //given

        //when
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/test/status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Timer timer = registry.find("http.server.requests").timer();
        //then
        assertThat(result.getResponse().getContentAsString()).isEqualTo("Status is ok");
        assertThat(timer).isNotNull();
        assertThat(timer.count()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void verifyThatTagsAreCalculatedForMetrics() throws Exception {
        //given
        when(tagsProvider.getStaticTags()).thenReturn(getStaticTags());
        when(tagsProvider.getDynamicTags(any(), any())).thenReturn(getDynamicTags());
        //when
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/test/status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        assertThat(result.getResponse().getContentAsString()).isEqualTo("Status is ok");
        //verify(tagsProvider, atLeastOnce()).getStaticTags(); - problem this is called during initlization of spring context
        verify(tagsProvider, atLeastOnce()).getDynamicTags(any(), any());

    }

    @Test
    @Order(3)
    public void verifyThatTagsAreCalculatedForMetricsMultipleTimes() throws Exception {
        //given
        when(tagsProvider.getStaticTags()).thenReturn(getStaticTags());
        when(tagsProvider.getDynamicTags(any(), any())).thenReturn(getDynamicTags());
        //when
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/test/status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult result2 = mvc.perform(MockMvcRequestBuilders.get("/test/status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult result3 = mvc.perform(MockMvcRequestBuilders.get("/test/status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        assertThat(result.getResponse().getContentAsString()).isEqualTo("Status is ok");
        assertThat(result2.getResponse().getContentAsString()).isEqualTo("Status is ok");
        assertThat(result3.getResponse().getContentAsString()).isEqualTo("Status is ok");
        verify(tagsProvider, times(3)).getDynamicTags(any(), any());

    }

    Map<String, String> getStaticTags() {
        Map<String, String> tags = new HashMap<>();
        tags.put("systemName", "unitTest");
        return tags;
    }

    Map<String, String> getDynamicTags() {
        Map<String, String> tags = new HashMap<>();
        tags.put("dynamicTag1", "dynamicTag1Value");
        return tags;
    }
}
