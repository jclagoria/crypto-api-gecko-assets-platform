package ar.com.api.assetPlatform.router;

import ar.com.api.assetPlatform.configuration.ApiServiceConfig;
import ar.com.api.assetPlatform.handler.HealthCoinGeckoApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HealthApiRouter {

    private final ApiServiceConfig apiServiceConfig;

    public HealthApiRouter(ApiServiceConfig serviceConfig) {
        this.apiServiceConfig = serviceConfig;
    }

    @Bean
    public RouterFunction<ServerResponse> routeHealth(HealthCoinGeckoApiHandler handler) {

        return RouterFunctions
                .route()
                .GET(apiServiceConfig.getBaseUrl() + apiServiceConfig.getHealthAPI(),
                        handler::getStatusServiceCoinGecko)
                .build();

    }

}
