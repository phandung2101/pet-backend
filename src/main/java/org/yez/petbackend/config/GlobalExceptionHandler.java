package org.yez.petbackend.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Component
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    protected void handleGlobalException(Exception ex, HttpServletResponse response) {
        log.error("Exception happened", ex.fillInStackTrace());
        response.setStatus(400);
    }
}
