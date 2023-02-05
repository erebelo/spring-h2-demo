package com.erebelo.springh2demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ExceptionResponse exception(Exception exception, HttpServletResponse response) {
        LOGGER.error("Exception thrown", exception);
        return parseExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), response);
    }

    @ResponseBody
    @ExceptionHandler(IllegalStateException.class)
    public ExceptionResponse illegalStateException(IllegalStateException exception, HttpServletResponse response) {
        LOGGER.error("IllegalStateException thrown", exception);
        return parseExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), response);
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse illegalArgumentException(IllegalArgumentException exception, HttpServletResponse response) {
        LOGGER.error("IllegalArgumentException thrown", exception);
        return parseExceptionMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), response);
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ExceptionResponse constraintViolationException(ConstraintViolationException exception, HttpServletResponse response) {
        LOGGER.error("ConstraintViolationException thrown", exception);
        return parseExceptionMessage(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage(), response);
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionResponse httpMessageNotReadableException(HttpMessageNotReadableException exception, HttpServletResponse response) {
        LOGGER.error("HttpMessageNotReadableException thrown", exception);
        return parseExceptionMessage(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage(), response);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletResponse response) {
        LOGGER.error("MethodArgumentNotValidException thrown", exception);

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String message = "No message found";
        if (!fieldErrors.isEmpty()) {
            message = fieldErrors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList()).toString();
        }
        return parseExceptionMessage(HttpStatus.UNPROCESSABLE_ENTITY, message, response);
    }

    @ResponseBody
    @ExceptionHandler(StandardException.class)
    public ExceptionResponse standardException(StandardException exception, HttpServletResponse response) {
        LOGGER.error("StandardException thrown", exception);
        return parseStandardExceptionMessage(exception, response);
    }

    private ExceptionResponse parseExceptionMessage(HttpStatus status, String message, HttpServletResponse response) {
        response.setStatus(status.value());
        return new ExceptionResponse(status, message, System.currentTimeMillis());
    }

    private ExceptionResponse parseStandardExceptionMessage(StandardException exception, HttpServletResponse response) {
        ExceptionResponse exceptionResponse;
        try {
            Integer errorCode = Integer.valueOf(exception.getError().toString().split("_")[1]);
            exceptionResponse = new ExceptionResponse(HttpStatus.valueOf(errorCode), String.format(exception.getError().getValue(),
                    exception.getArgs()), System.currentTimeMillis());
        } catch (Exception e) {
            exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Basic error handling exception: %s", e),
                    System.currentTimeMillis());
        }
        response.setStatus(exceptionResponse.getStatus().value());
        return exceptionResponse;
    }
}
