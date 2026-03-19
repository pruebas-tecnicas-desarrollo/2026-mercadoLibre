package com.hackerrank.sample.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-Correlation-Id";;
    private static final String MDC_KEY = "correlationId";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        String id = request.getHeader(HEADER);

        // Use client-provided request id when present; otherwise generate one.
        if (id == null || id.isBlank()) {

            // UTC timestamp is used for simplicity and clarity, given the small scope of the application.
            id = OffsetDateTime.now(ZoneOffset.UTC).format(FORMATTER);
        }

        // Put correlation id into MDC so every log line can include it (via log pattern).
        MDC.put(MDC_KEY, id);

        // Echo it back so clients can correlate requests with server logs.
        response.setHeader(HEADER, id);

        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long ms = System.currentTimeMillis() - start;

            // Log one line per request, even if an exception is thrown before reaching the controller.
            log.info("finished request information: HTTP {} {} -> {} ({}ms)",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    ms
            );

            MDC.remove(MDC_KEY);
        }
    }
}
