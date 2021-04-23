package com.moro.commons.context.http;

import java.util.Map;

public interface ApplicationHttpContext {
    void initializeContextWithHeaders(Map<String, String> headers);
    Map<String, String> getContextHeaders();
}
