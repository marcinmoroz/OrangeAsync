package com.moro.test.feignclienttest.tools;

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
    StopWatch requestWatcher;
    @Autowired
    private Tracer tracer;

    @Override
    protected void log(String s, String s1, Object... objects) {

    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        requestWatcher = new StopWatch();
        requestWatcher.start();
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
        if(requestWatcher != null) {
            requestWatcher.stop();
        }
        log.info("Time : " + requestWatcher.getTotalTimeSeconds());
        if (response.body() != null) {
            String result="";
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            int bodyLength = bodyData.length;
            if (bodyLength > 0) {
                result = Util.decodeOrDefault(bodyData, Util.UTF_8, "Binary data");
            }
            Response build = response.toBuilder().body(bodyData).build();
            Request request = build.request();
            String requestBody = request.toString();
            String bodyText =  request.charset() != null ? new String(request.body(), request.charset()) : null;
            //log.info("B" + bodyText);
            log.info("Request : " + requestBody);
            log.info("List of requestheaders" );
            request.headers().forEach((s, strings) -> {
                log.info("Header : " + s);
                log.info(String.join(";",strings));
            });
            // request URL request.url()
            // request parameter bodyText
            // request result
            // request time elapsedTime
            //logger.info();
            return build;
        }
        return response;
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        if(requestWatcher != null) {
            requestWatcher.stop();
        }
        return super.logIOException(configKey, logLevel, ioe, elapsedTime);
    }
}
