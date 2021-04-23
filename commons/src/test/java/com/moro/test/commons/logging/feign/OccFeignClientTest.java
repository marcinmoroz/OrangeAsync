package com.moro.test.commons.logging.feign;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.moro.commons.context.http.ApplicationHttpContext;
import com.moro.commons.logging.OccFeignLogger;
import com.moro.test.commons.logging.TestHttpContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import nl.altindag.log.LogCaptor;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FeignTestConfig.class)
public class OccFeignClientTest {
    @Autowired
    FeignTestClient client;
    @Autowired
    private WireMockServer mockService;
    @Autowired
    ApplicationHttpContext testContext;

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

    @Test
    public void shouldPassContext() {
        //        Given
        String expectedResponse = "HELLO WORLD";
        String passedContext = "Value1";
        TestHttpContext context = (TestHttpContext)testContext;
        ((TestHttpContext) testContext).setContextValue(passedContext);
        mockService.stubFor(WireMock.get(
                WireMock.urlEqualTo("/test")).
                withHeader("contextValue", equalTo(passedContext)).
                willReturn(WireMock.aResponse().withBody(expectedResponse)
        ));
        //        When
        String testResult = client.test();
        //        Then
        assertThat(context.getContextValue()).isEqualTo(passedContext);
        assertNotNull(expectedResponse);
    }
}