package ar.com.api.assetPlatform.handler;

import ar.com.api.assetPlatform.exception.ApiCustomException;
import ar.com.api.assetPlatform.services.AssetPlatformService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class AssetPlatformApiHandler {

    private AssetPlatformService serviceAssetPlatform;

    public Mono<ServerResponse> getAssertPlatformCoinGecko(ServerRequest sRequest) {
        log.info("In getAssertPlatformCoinGecko");

        return serviceAssetPlatform.getAssetPlatform()
                .collectList()
                .flatMap(
                        assetPlatforms -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(assetPlatforms))
                .doOnSubscribe(subscription -> log.info("Retrieving list of asset platforms"))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(error -> Mono
                        .error(new ApiCustomException("An expected error occurred in getAssertPlatformCoinGecko",
                                HttpStatus.INTERNAL_SERVER_ERROR))
                );
    }

}
