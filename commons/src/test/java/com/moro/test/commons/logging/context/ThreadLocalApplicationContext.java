package com.moro.test.commons.logging.context;

import com.moro.commons.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalApplicationContext implements ApplicationContext {
    private static ThreadLocal<Map<String, String>> context = new ThreadLocal<>();
    private final static String _sessionIdKey = "SessionId";

    public void setSessionId(String value) {
        getContext().put(_sessionIdKey, value);
    }

    public String getSessionId() {
        return getContext().get(_sessionIdKey);
    }

    @Override
    public Map<String, String> getCopyOfContext() {
        return getContext();
    }

    @Override
    public void initializeContext(Map<String, String> contextData) {
        context.set(contextData);
    }

    @Override
    public void clearContext() {
        context.set(new HashMap<>());
    }

    private Map<String, String> getContext() {
        Map<String, String> ctx = context.get();
        if(ctx == null) {
            ctx = new HashMap<>();
            initializeContext(ctx);
        }
        return ctx;
    }
}
