package com.softgen.demo.exceptions;

import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

  private HttpStatus status;
  private String message;
  private String error;

  public ApiError(HttpStatus status, String message, String error) {
    this.status = status;
    this.message = message;
    this.error = error;
  }
}