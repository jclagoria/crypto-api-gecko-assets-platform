package ar.com.api.assetPlatform.services;

import ar.com.api.assetPlatform.configuration.ExternalServerConfig;
import ar.com.api.assetPlatform.configuration.HttpServiceCall;
import ar.com.api.assetPlatform.model.AssetPlatform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AssetPlatformService {

    private final ExternalServerConfig externalServerConfig;

    private final HttpServiceCall httpServiceCall;

    public AssetPlatformService(HttpServiceCall httpServiceCall, ExternalServerConfig serverConfig) {
        this.httpServiceCall = httpServiceCall;
        this.externalServerConfig = serverConfig;
    }

    public Flux<AssetPlatform> getAssetPlatform() {

        log.info("Calling method: getAssetPlatform(): {}", externalServerConfig.getUrlAssertPlatform());

        return httpServiceCall.getFluxObject(externalServerConfig.getUrlAssertPlatform(),
                AssetPlatform.class);
    }

}
