package com.javaassign5.ex4;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/** Logs every request and how long it took. Registered with the @WebFilter annotation. */
@WebFilter(filterName = "RequestLoggingFilter", urlPatterns = "/*")
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        long start = System.nanoTime();
        try {
            chain.doFilter(request, response);
        } finally {
            HttpServletRequest http = (HttpServletRequest) request;
            long ms = (System.nanoTime() - start) / 1_000_000;
            request.getServletContext().log(http.getMethod() + " " + http.getRequestURI() + " (" + ms + " ms)");
        }
    }
}
