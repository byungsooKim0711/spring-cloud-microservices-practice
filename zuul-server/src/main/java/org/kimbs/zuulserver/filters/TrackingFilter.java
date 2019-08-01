package org.kimbs.zuulserver.filters;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.netflix.zuul.ZuulFilter;

import java.util.UUID;

@Component
@Slf4j
public class TrackingFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;

    @Autowired
    private FilterUtils filterUtils;

    @Override
    public String filterType() {
        return filterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    @Override
    public Object run() throws ZuulException {
        if (isCorrelationIpPresent()) {
            log.info("tmx-correlation-id found in tracking filter: {}.", filterUtils.getCorrelationId());
        } else {
            filterUtils.setCorrelationId(generateCorrelationId());
            log.info("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
        }

        RequestContext requestContext = RequestContext.getCurrentContext();
        log.info("Processing incoming request for {}.", requestContext.getRequest().getRequestURI());

        return null;
    }

    private boolean isCorrelationIpPresent() {
        if (filterUtils.getCorrelationId() != null) {
            return true;
        }
        else {
            return false;
        }
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
