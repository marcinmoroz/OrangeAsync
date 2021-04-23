package com.moro.commons.context;

import java.util.Map;

public interface ApplicationContext {
    Map<String,String> getCopyOfContext();
    void initializeContext(Map<String,String> contextData);
    void clearContext();
}
