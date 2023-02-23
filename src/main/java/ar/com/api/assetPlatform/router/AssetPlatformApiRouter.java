package ar.com.api.assetPlatform.router;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import ar.com.api.assetPlatform.handler.AssetPlatformApiHandler;
@Configuration
public class AssetPlatformApiRouter {

 @Value("${coins.baseURL}")
 private String URL_SERVICE_API;

 @Value("${coins.healthAPI}")
 private String URL_HEALTH_GECKO_API;

 @Value("${coins.urlAssertPlatform}")
 private String URL_ASSET_PLATFORM_API; 
 
 @Bean
 public RouterFunction<ServerResponse> route(AssetPlatformApiHandler handler) {

  return RouterFunctions
            .route()
            .GET(URL_SERVICE_API + URL_HEALTH_GECKO_API, 
                        handler::getStatusServiceCoinGecko)
            .GET(URL_SERVICE_API + URL_ASSET_PLATFORM_API, 
                        handler::getAssertPlatformCoinGecko)
            .build();

 }

}
