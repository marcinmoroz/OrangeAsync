package com.moro.commons.context.monitoring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface PrometheusTagsProvider {
    Map<String, String> getStaticTags();
    Map<String, String> getDynamicTags(HttpServletRequest request, HttpServletResponse response);
}
