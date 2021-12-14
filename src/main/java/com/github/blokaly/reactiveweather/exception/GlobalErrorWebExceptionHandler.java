package com.github.blokaly.reactiveweather.exception;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

  public GlobalErrorWebExceptionHandler(
      ErrorAttributes errorAttributes,
      WebProperties webProperties,
      ApplicationContext applicationContext) {
    super(errorAttributes, webProperties.getResources(), new ErrorProperties(), applicationContext);
    var conf = new DefaultServerCodecConfigurer();
    super.setMessageWriters(conf.getWriters());
    super.setMessageReaders(conf.getReaders());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }
}
