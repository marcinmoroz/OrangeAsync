package com.moro.test.commons.logging;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = FeignTestConfig.class)
public class OccFeignLoggerTest {
    @Autowired
    FeignTestClient client;

    @Test
    public void shouldLogRequest() {
        //        Given

        //        When

        //        Then
        assertTrue(true);
        assertNotNull(client);
    }
}