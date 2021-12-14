package com.github.blokaly.reactiveweather.exception;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request,
                                                ErrorAttributeOptions options) {
    var attributes = super.getErrorAttributes(request, options);
    if (getError(request) instanceof GlobalException) {
      GlobalException ex = (GlobalException) getError(request);
      attributes.put("exception", ex.getClass().getSimpleName());
      attributes.put("message", ex.getMessage());
      attributes.put("status", ex.getStatus().value());
      attributes.put("error", ex.getStatus().getReasonPhrase());
      return attributes;
    }

    attributes.put("exception", "SystemException");
    attributes.put("message", "System Error , Check logs!");
    attributes.put("status", "500");
    attributes.put("error", " System Error ");
    return attributes;
  }
}
