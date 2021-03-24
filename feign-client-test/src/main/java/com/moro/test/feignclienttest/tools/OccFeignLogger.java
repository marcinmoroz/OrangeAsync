package com.moro.test.feignclienttest.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import io.opentracing.Tracer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import java.io.IOException;

@Log4j2
public class OccFeignLogger extends Logger {
    @Autowired
    private Tracer tracer;

    @Override
    protected void log(String s, String s1, Object... objects) {

    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        log.info("logRequest Config key : " + configKey);
        String requestBody = request.toString();
        log.info("Request start");
        log.info("Request : " + requestBody);
        log.info("Headers" + request.headers());
        request.headers().forEach((s, strings) -> {
            log.info("Header : " + s);
            log.info(String.join(";",strings));
        });
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        String reason = response.reason() != null && logLevel.compareTo(Level.NONE) > 0 ? " " + response.reason()
                        : "";
        int status = response.status();
        int bodyLength = 0;
        // HTTP 204 No Content "...response MUST NOT include a message-body"
        // HTTP 205 Reset Content "...response MUST NOT include an entity"
        RequestLog requestLog = new RequestLog();
        Request request = response.request();
        String requestBody = request.toString();
        String requestBodyText =  request.charset() != null ? new String(request.body(), request.charset()) : null;
        requestLog.request = requestBodyText;
        requestLog.uri = request.url();
        requestLog.elapsedTime = elapsedTime;
        requestLog.status = status;
        requestLog.reason = reason;
        request.headers().forEach((header, headerValues) -> {
            requestLog.requestHeaders.put(header, headerValues);
        });
        if (response.body() != null && !(status == 204 || status == 205)) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            bodyLength = bodyData.length;
            String responseBody="";
            if (bodyLength > 0) {
                responseBody = Util.decodeOrDefault(bodyData, Util.UTF_8, "Binary data");
            }
            response.headers().forEach((header, headerValues) -> {
                requestLog.responseHeaders.put(header, headerValues);
            });
            requestLog.response = responseBody;
            response = response.toBuilder().body(bodyData).build();
        }
        ObjectMapper mapper = new ObjectMapper();
        log.info(mapper.writeValueAsString(requestLog));
        return response;
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        log.info("logIOException Config key : " + configKey);
        return super.logIOException(configKey, logLevel, ioe, elapsedTime);
    }
}
