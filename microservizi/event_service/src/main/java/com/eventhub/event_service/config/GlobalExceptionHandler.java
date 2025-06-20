package com.eventhub.event_service.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.eventhub.event_service.dto.ErrorResponse;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        @NonNull MethodArgumentNotValidException ex,
                        @NonNull HttpHeaders headers,
                        @NonNull HttpStatusCode status,
                        @NonNull WebRequest request) {

                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult().getAllErrors()
                                .forEach(error -> {
                                        String fieldName;
                                        if (error instanceof FieldError) {
                                                fieldName = ((FieldError) error).getField();
                                        } else {
                                                // Per errori a livello di classe (come la nostra validazione custom)
                                                fieldName = error.getObjectName();
                                        }
                                        errors.put(fieldName, error.getDefaultMessage());
                                });

                ErrorResponse errorResponse = new ErrorResponse(
                                new Date().toString(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Richiesta non valida",
                                errors,
                                request.getDescription(false));

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleAllExceptions(@NotNull Exception ex, @NotNull WebRequest request) {
                Map<String, String> errors = new HashMap<>();
                errors.put("error", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                new Date().toString(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Errore interno del server",
                                errors,
                                request.getDescription(false));
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraintViolationException(
                        @NotNull ConstraintViolationException ex,
                        @NotNull WebRequest request) {
                Map<String, String> errors = new HashMap<>();
                errors.put("error", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                new Date().toString(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Richiesta non valida",
                                errors,
                                request.getDescription(false));
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ErrorResponse> handleResponseStatusException(@NotNull ResponseStatusException ex,
                        @NotNull WebRequest request) {
                Map<String, String> errors = new HashMap<>();
                errors.put("error", ex.getReason());
                // sendMail(ex, request);
                ErrorResponse errorResponse = new ErrorResponse(
                                new Date().toString(),
                                ex.getStatusCode().value(),
                                ex.getReason(),
                                errors,
                                request.getDescription(false));
                return new ResponseEntity<ErrorResponse>(errorResponse, ex.getStatusCode());
        }
}