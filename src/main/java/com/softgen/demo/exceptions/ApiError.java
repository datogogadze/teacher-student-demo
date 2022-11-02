package com.softgen.demo.exceptions;

import java.time.ZonedDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

  private ZonedDateTime timestamp = ZonedDateTime.now();
  private int status;
  private String message;
  private String error;

  public ApiError(int status, String message, String error) {
    this.status = status;
    this.message = message;
    this.error = error;
  }
}