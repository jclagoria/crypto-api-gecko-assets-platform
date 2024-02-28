package ar.com.api.assetPlatform.handler;

import ar.com.api.assetPlatform.model.AssetPlatform;
import ar.com.api.assetPlatform.services.AssetPlatformService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        return ServerResponse
                .ok()
                .body(
                        serviceAssetPlatform.getAssetPlatform(),
                        AssetPlatform.class);
    }

}
