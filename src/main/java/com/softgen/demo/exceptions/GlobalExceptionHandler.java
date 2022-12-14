package com.softgen.demo.exceptions;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private ResponseEntity<Object> toResponse(ApiError apiError, Exception ex,
                                            HttpHeaders headers, WebRequest request
                                           ) {
    return new ResponseEntity<>(apiError, headers, apiError.getStatus());
  }

  @ExceptionHandler(value = {EntityNotFoundException.class})
  protected ResponseEntity<Object> handleEntityNotFoundException(RuntimeException ex,
                                                                 WebRequest request
                                                                ) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Not found",
                                     ex.getLocalizedMessage());
    return toResponse(apiError, ex, new HttpHeaders(), request);
  }

  @ExceptionHandler(value = {EntityExistsException.class})
  protected ResponseEntity<Object> handleEntityExistsException(RuntimeException ex,
                                                               WebRequest request
                                                              ) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Already exists",
                                     ex.getLocalizedMessage());
    return toResponse(apiError, ex, new HttpHeaders(), request);
  }

  @ExceptionHandler(value = {RuntimeException.class})
  protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex,
                                                          WebRequest request
                                                         ) {
    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Runtime exception",
                                     "Internal server error");
    return toResponse(apiError, ex, new HttpHeaders(), request);
  }


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request
                                                               ) {
    ApiError apiError = null;
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation error",
                              "\"" + error.getField() + "\" " + error.getDefaultMessage());
      break;
    }
    if (apiError != null) {
      for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
        apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation error",
                                "\"" + error.getObjectName() + "\"" + error.getDefaultMessage());
        break;
      }
    }
    return toResponse(apiError, ex, headers, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                      HttpStatus status, WebRequest request
                                                     ) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Type mismatch",
                                     ex.getLocalizedMessage());
    return toResponse(apiError, ex, new HttpHeaders(), request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request
                                                               ) {
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Not readable",
                                     ex.getLocalizedMessage());
    return toResponse(apiError, ex, new HttpHeaders(), request);
  }

}