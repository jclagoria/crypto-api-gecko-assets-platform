package ar.com.api.assetPlatform.router;

import ar.com.api.assetPlatform.configuration.ApiServiceConfig;
import ar.com.api.assetPlatform.handler.AssetPlatformApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AssetPlatformApiRouter {

    private final ApiServiceConfig apiServiceConfig;

    public AssetPlatformApiRouter(ApiServiceConfig serviceConfig) {
        this.apiServiceConfig = serviceConfig;
    }

    @Bean
    public RouterFunction<ServerResponse> routeAsset(AssetPlatformApiHandler handler) {

        return RouterFunctions
                .route()
                .GET(apiServiceConfig.getBaseUrl() + apiServiceConfig.getUrlAssertPlatform(),
                        handler::getAssertPlatformCoinGecko)
                .build();

    }

}
