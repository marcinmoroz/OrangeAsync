package com.moro.commons.context.http;

import java.util.Map;

public interface ApplicationContextThroughHeaders {
    void initializeContextWithHeaders(Map<String, String> headers);
    Map<String, String> getContextHeaders();
}
