package com.recipes.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    private static final String HANDLED_ACTION_FIELD_LOG_TEMPLATE = "handled_action={}";

    @org.springframework.web.bind.annotation.ExceptionHandler({BigQuerySearchException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handleSearchException(final RuntimeException exception) {
        return logExceptionAndGetMessage("big_query_failure_clause", exception);
    }

    private String logExceptionAndGetMessage(final String actionName, final Exception exception) {
        log.warn(HANDLED_ACTION_FIELD_LOG_TEMPLATE, actionName, exception);
        return exception.getMessage();
    }
}
