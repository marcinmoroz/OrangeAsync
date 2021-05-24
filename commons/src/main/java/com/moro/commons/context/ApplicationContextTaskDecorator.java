package com.moro.commons.context;

import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ApplicationContextTaskDecorator implements TaskDecorator {
    final List<ApplicationContext> contexts;

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<ApplicationContext, Map<String,String>> copyOfContextsMaps = contexts.stream().
                collect(Collectors.toMap(applicationContext -> applicationContext,
                applicationContext -> applicationContext.getCopyOfContext()));
        return () -> {
          try {
              copyOfContextsMaps.forEach( (ct, map) -> ct.initializeContext(map));
              runnable.run();
          }
          finally {
              copyOfContextsMaps.forEach( (ct, map) -> ct.clearContext());
          }

        };
    }
}
