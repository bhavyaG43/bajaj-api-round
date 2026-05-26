package com.bajaj.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Servlet filter that logs every HTTP request and its corresponding response.
 * Runs for all routes automatically via @Component registration.
 */
@Component
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest  httpReq  = (HttpServletRequest)  request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        long start = System.currentTimeMillis();

        log.info("▶ REQUEST  │ method={} │ uri={} │ remoteAddr={}",
                httpReq.getMethod(),
                httpReq.getRequestURI(),
                httpReq.getRemoteAddr());

        chain.doFilter(request, response);

        long elapsed = System.currentTimeMillis() - start;

        log.info("◀ RESPONSE │ status={} │ uri={} │ elapsed={}ms",
                httpResp.getStatus(),
                httpReq.getRequestURI(),
                elapsed);
    }
}
