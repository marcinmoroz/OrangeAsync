package com.moro.test.commons.monitoring;

import com.moro.commons.context.monitoring.PrometheusTagsProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class TestPrometheusTagsProvider implements PrometheusTagsProvider {
    @Override
    public Map<String, String> getStaticTags() {
        Map<String, String> tags = new HashMap<>();
        tags.put("systemName", "unitTest");
        return tags;
    }

    @Override
    public Map<String, String> getDynamicTags(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> tags = new HashMap<>();
        tags.put("dynamicTag1", "dynamicTag1Value");
        return tags;
    }
}
