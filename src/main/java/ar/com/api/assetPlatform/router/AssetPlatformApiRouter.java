package ar.com.api.assetPlatform.router;

import ar.com.api.assetPlatform.exception.CoinGeckoDataNotFoudException;
import ar.com.api.assetPlatform.handler.AssetPlatformApiHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

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

    @Bean
    public WebExceptionHandler exceptionHandler() {
        return (ServerWebExchange exchange, Throwable ex) -> {
            if (ex instanceof CoinGeckoDataNotFoudException) {
                exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
                return exchange.getResponse().setComplete();
            }
            return Mono.error(ex);
        };
    }

}
