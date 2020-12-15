package com.moro.test.feignclienttest.tools;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.StopWatch;

import java.io.IOException;

@Log4j2
public class OccFeignLogger extends Logger {
    StopWatch requestWatcher;
    @Override
    protected void log(String s, String s1, Object... objects) {

    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        requestWatcher = new StopWatch();
        requestWatcher.start();
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        if(requestWatcher != null) {
            requestWatcher.stop();
        }
        if (response.body() != null) {
            String result="";
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            int bodyLength = bodyData.length;
            if (bodyLength > 0) {
                result = Util.decodeOrDefault(bodyData, Util.UTF_8, "Binary data");
            }
            Response build = response.toBuilder().body(bodyData).build();
            Request request = build.request();
            String bodyText =  request.charset() != null ? new String(request.body(), request.charset()) : null;
            log.info("Time : " + requestWatcher.getTotalTimeSeconds());
            log.info(bodyText);
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
