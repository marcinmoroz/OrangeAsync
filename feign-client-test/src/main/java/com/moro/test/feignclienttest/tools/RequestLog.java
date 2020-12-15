package com.moro.test.feignclienttest.tools;

import lombok.Data;

import java.io.IOException;

@Data
public class RequestLog {
    IOException error;
    String request;
    String response;
}
