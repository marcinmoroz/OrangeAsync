package com.moro.commons.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class OccFeignLogger extends Logger {
    @Override
    protected void log(String s, String s1, Object... objects) {

    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        super.logRequest(configKey, logLevel, request);
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        String reason = response.reason() != null && logLevel.compareTo(Level.NONE) > 0 ? " " + response.reason()
                        : "";
        int status = response.status();
        int bodyLength = 0;
        OutgoingRequestLog outgoingRequestLog = new OutgoingRequestLog();
        Request request = response.request();
        String requestBody = request.toString();
        String requestBodyText =  request.charset() != null ? new String(request.body(), request.charset()) : null;
        outgoingRequestLog.request = requestBodyText;
        outgoingRequestLog.uri = request.url();
        outgoingRequestLog.elapsedTimeMs = elapsedTime;
        outgoingRequestLog.status = status;
        outgoingRequestLog.reason = reason;
        request.headers().forEach((header, headerValues) -> {
            outgoingRequestLog.requestHeaders.put(header, headerValues);
        });
        // HTTP 204 No Content "...response MUST NOT include a message-body"
        // HTTP 205 Reset Content "...response MUST NOT include an entity"
        if (response.body() != null && !(status == 204 || status == 205)) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            bodyLength = bodyData.length;
            String responseBody="";
            if (bodyLength > 0) {
                responseBody = Util.decodeOrDefault(bodyData, Util.UTF_8, "Binary data");
            }
            response.headers().forEach((header, headerValues) -> {
                outgoingRequestLog.responseHeaders.put(header, headerValues);
            });
            outgoingRequestLog.response = responseBody;
            response = response.toBuilder().body(bodyData).build();
        }
        ObjectMapper mapper = new ObjectMapper();
        log.info(mapper.writeValueAsString(outgoingRequestLog));
        return response;
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        //log.info("logIOException Config key : " + configKey);
        return super.logIOException(configKey, logLevel, ioe, elapsedTime);
    }
}
