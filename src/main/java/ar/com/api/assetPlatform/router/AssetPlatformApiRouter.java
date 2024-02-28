package ar.com.api.assetPlatform.router;

import ar.com.api.assetPlatform.handler.AssetPlatformApiHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AssetPlatformApiRouter {

    @Value("${coins.baseURL}")
    private String URL_SERVICE_API;

    @Value("${coins.urlAssertPlatform}")
    private String URL_ASSET_PLATFORM_API;

    @Bean
    public RouterFunction<ServerResponse> routeAsset(AssetPlatformApiHandler handler) {

        return RouterFunctions
                .route()
                .GET(URL_SERVICE_API + URL_ASSET_PLATFORM_API,
                        handler::getAssertPlatformCoinGecko)
                .build();

    }

}
