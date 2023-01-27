package org.pk.route.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RouteNotFoundException.class, CountryNotFoundException.class})
    public ResponseEntity<Object> routeNotFound(RuntimeException ex, WebRequest request) {
        ProblemDetail problemDetail = null;
        if (ex instanceof RouteNotFoundException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Route not found");
        } else if (ex instanceof CountryNotFoundException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Country not found");
        }
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<Object> applicationError500(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Application error"),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
