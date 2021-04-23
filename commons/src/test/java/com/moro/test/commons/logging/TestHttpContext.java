package com.moro.test.commons.logging;

import com.moro.commons.context.http.ApplicationHttpContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data
public class TestHttpContext implements ApplicationHttpContext {
    String contextValue;;

    @Override
    public void initializeContextWithHeaders(Map<String, String> headers) {
        contextValue = headers.get("contextValue");
    }

    @Override
    public Map<String, String> getContextHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("contextValue", contextValue);
        return headers;
    }
}
