package com.moro.test.commons.logging;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import nl.altindag.log.LogCaptor;

import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FeignTestConfig.class)
public class OccFeignLoggerTest {
    @Autowired
    FeignTestClient client;
    @Autowired
    private WireMockServer mockService;

    @Test
    public void shouldLogRequest() {
        //        Given
        String expectedResponse = "HELLO WORLD";
        LogCaptor logCaptor = LogCaptor.forClass(OccFeignLogger.class);
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/test")).willReturn(
                    WireMock.aResponse().withBody(expectedResponse)
                ));

        //        When
        String testResult = client.test();

        //        Then
        assertNotNull(expectedResponse);
        List<String> logs = logCaptor.getLogs();
        assertThat(logs).hasSize(1);
        String log = logs.get(0);
        assertThat(log).contains("\"uri\":\"http://localhost:9561/test\"");
        assertThat(log).contains("\"response\":\"HELLO WORLD\"");

    }
}