package pl.orange.opl.newcustomeraccountasync.tools;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//public class CustomeWebMvcTagsProvider implements WebMvcTagsProvider {
//    @Override
//    public Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response, Object handler, Throwable exception) {
//        return Tags.of()
//    }
//
//    @Override
//    public Iterable<Tag> getLongRequestTags(HttpServletRequest request, Object handler) {
//        return null;
//    }
//}
