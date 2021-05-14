package com.moro.test.commons.logging.context;

import com.moro.commons.context.ApplicationContextTaskDecorator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextPassingTest {

    private ThreadPoolTaskExecutor defaultExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        return executor;
    }

    private ThreadPoolTaskExecutor executorWithTaskDecorator() {
        ThreadLocalApplicationContext context = new ThreadLocalApplicationContext();
        ApplicationContextTaskDecorator decorator =
                new ApplicationContextTaskDecorator(Arrays.asList(context));
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(decorator);
        executor.initialize();
        return executor;
    }

    @SneakyThrows
    @Test
    public void contextIsNotPassed() {
        //        Given
        String sessionId = UUID.randomUUID().toString();
        ThreadLocalApplicationContext context = new ThreadLocalApplicationContext();
        context.setSessionId(sessionId);
        //        When
        Callable<String> task = () -> {
            ThreadLocalApplicationContext contextOnNewThread = new ThreadLocalApplicationContext();
            return contextOnNewThread.getSessionId();
        };
        Future<String> calculatedSessionId = defaultExecutor().submit(task);
        String readSessionId = calculatedSessionId.get();
        //        Then
        assertThat(readSessionId).isEqualTo(null);
    }

    @SneakyThrows
    @Test
    public void contextIsPassed() {
        //        Given
        String sessionId = UUID.randomUUID().toString();
        ThreadLocalApplicationContext context = new ThreadLocalApplicationContext();
        context.setSessionId(sessionId);
        //        When
        Callable<String> task = () -> {
            ThreadLocalApplicationContext contextOnNewThread = new ThreadLocalApplicationContext();
            return contextOnNewThread.getSessionId();
        };
        Future<String> calculatedSessionId = executorWithTaskDecorator().submit(task);
        String readSessionId = calculatedSessionId.get();
        //        Then
        assertThat(readSessionId).isEqualTo(sessionId);
        assertThat(context.getSessionId()).isEqualTo(sessionId);
    }
}
