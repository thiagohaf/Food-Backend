package com.thiagoferreira.food_backend.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String PROBLEM_TYPE_BASE_URI = "https://api.food-backend.com/problems/";
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "resource-not-found"));
        problemDetail.setTitle("Resource Not Found");
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        log.error("Unauthorized access: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "unauthorized"));
        problemDetail.setTitle("Unauthorized");
        
        // Set instance URI
        if (request instanceof ServletWebRequest servletWebRequest) {
            String requestURI = servletWebRequest.getRequest().getRequestURI();
            problemDetail.setInstance(URI.create(servletWebRequest.getRequest().getRequestURL().toString()));
            problemDetail.setProperty("path", requestURI);
            problemDetail.setProperty("method", servletWebRequest.getRequest().getMethod());
        } else {
            String description = request.getDescription(false);
            if (description.startsWith("uri=")) {
                problemDetail.setInstance(URI.create(description.substring(4)));
            }
        }
        
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(problemDetail);
    }

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ProblemDetail> handleDomainValidationException(DomainValidationException ex) {
        log.error("Domain validation error: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "domain-validation-error"));
        problemDetail.setTitle("Domain Validation Error");
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed"
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "validation-error"));
        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("errors", errors);
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation: {}", ex.getMessage());
        
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Constraint violation"
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "constraint-violation"));
        problemDetail.setTitle("Constraint Violation");
        problemDetail.setProperty("errors", errors);
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Malformed JSON or missing request body: {}", ex.getMessage());
        
        String detail = "Request body is malformed or missing. Please check your JSON format.";
        if (ex.getMessage() != null && ex.getMessage().contains("Required request body is missing")) {
            detail = "Request body is required but was not provided.";
        }
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                detail
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "malformed-request"));
        problemDetail.setTitle("Malformed Request");
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("Missing required parameter: {}", ex.getParameterName());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                String.format("Required parameter '%s' is missing", ex.getParameterName())
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "missing-parameter"));
        problemDetail.setTitle("Missing Required Parameter");
        problemDetail.setProperty("parameter", ex.getParameterName());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("Type mismatch for parameter '{}': expected {}, but got '{}'", 
                ex.getName(), ex.getRequiredType(), ex.getValue());
        
        String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                String.format("Invalid value '%s' for parameter '%s'. Expected type: %s", 
                        ex.getValue(), ex.getName(), expectedType)
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "type-mismatch"));
        problemDetail.setTitle("Type Mismatch");
        problemDetail.setProperty("parameter", ex.getName());
        problemDetail.setProperty("expectedType", expectedType);
        problemDetail.setProperty("providedValue", ex.getValue() != null ? ex.getValue().toString() : "null");
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("HTTP method '{}' is not supported for this endpoint. Supported methods: {}", 
                ex.getMethod(), ex.getSupportedHttpMethods());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.METHOD_NOT_ALLOWED,
                String.format("HTTP method '%s' is not supported for this endpoint", ex.getMethod())
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "method-not-allowed"));
        problemDetail.setTitle("Method Not Allowed");
        problemDetail.setProperty("method", ex.getMethod());
        problemDetail.setProperty("supportedMethods", ex.getSupportedHttpMethods());
        
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(problemDetail);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error("Media type '{}' is not supported. Supported types: {}", 
                ex.getContentType(), ex.getSupportedMediaTypes());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                String.format("Media type '%s' is not supported", ex.getContentType())
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "unsupported-media-type"));
        problemDetail.setTitle("Unsupported Media Type");
        problemDetail.setProperty("contentType", ex.getContentType() != null ? ex.getContentType().toString() : "null");
        problemDetail.setProperty("supportedTypes", ex.getSupportedMediaTypes());
        
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(problemDetail);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ProblemDetail> handleMissingPathVariableException(MissingPathVariableException ex) {
        log.error("Missing path variable: {}", ex.getVariableName());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                String.format("Required path variable '%s' is missing", ex.getVariableName())
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "missing-path-variable"));
        problemDetail.setTitle("Missing Path Variable");
        problemDetail.setProperty("variable", ex.getVariableName());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.error("No handler found for {} {}", ex.getHttpMethod(), ex.getRequestURL());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL())
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "endpoint-not-found"));
        problemDetail.setTitle("Endpoint Not Found");
        problemDetail.setProperty("method", ex.getHttpMethod());
        problemDetail.setProperty("path", ex.getRequestURL());
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "internal-server-error"));
        problemDetail.setTitle("Internal Server Error");
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problemDetail);
    }
}

