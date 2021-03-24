package com.moro.test.feignclienttest.tools;

import lombok.Data;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RequestLog {
    public int status;
    public String reason;
    IOException error;
    String uri;
    String request;
    String response;
    long elapsedTime;
    Map<String, Collection<String>> requestHeaders = new HashMap<>();
    Map<String, Collection<String>> responseHeaders = new HashMap<>();
}
