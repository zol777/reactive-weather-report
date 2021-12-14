package com.github.blokaly.reactiveweather.exception;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GlobalException extends ResponseStatusException {

  public GlobalException(HttpStatus status, String message) {
    super(status, message);
  }

  public GlobalException(HttpStatus status, String message, Throwable e) {
    super(status, message, e);
  }

  @Override
  public String getMessage() {
    var reason = getReason();
    String msg = reason != null ? reason : "";
    return NestedExceptionUtils.buildMessage(msg, this.getCause());
  }
}
