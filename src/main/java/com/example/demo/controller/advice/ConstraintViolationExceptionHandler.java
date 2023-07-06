package com.example.demo.controller.advice;

import com.example.demo.dto.response.Error;
import com.example.demo.dto.response.ValidationError;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ConstraintViolationExceptionHandler extends ResponseEntityExceptionHandler {
    List<ValidationError> validationErrors = new ArrayList<>();

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationError validationError = new ValidationError();
        validationError.setStatus(status);
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                validationError.getErrors().add(Error
                        .builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build()));

//        validationErrors.add(validationError);

        return handleExceptionInternal(ex, validationError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationError validationError = new ValidationError();
        validationError.setStatus(status);
        Error error = new Error();
        error.setMessage(ex.getCause().getMessage().split("\\n")[0]);
        error.setField(ex.getMessage().split("\\n")[1].split("\"")[1]);
        validationError.setErrors(Arrays.asList(error));

//        validationErrors.add(validationError);
        return handleExceptionInternal(ex, validationError, headers, status, request);
    }
}
