package ar.com.api.ApiDemo.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import ar.com.api.ApiDemo.handler.ApiStatusHandler;

@Configuration
public class ApiRouter {
 
 private final String URL_SERVICE_API = "api-services";

 private final String URL_HEALTH_GECKO_API = "/health-api";
 
 @Bean
 public RouterFunction<ServerResponse> route(ApiStatusHandler handler) {

  return RouterFunctions
            .route()
            .GET(URL_SERVICE_API + URL_HEALTH_GECKO_API, 
                        handler::getStatusServiceCoinGecko)
            .build();

 }

}
