package ar.com.api.assetPlatform.services;

import ar.com.api.assetPlatform.configuration.HttpServiceCall;
import ar.com.api.assetPlatform.model.AssetPlatform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AssetPlatformService {

    @Value("${api.urlAssertPlatform}")
    private String URL_ASSET_PLATFORM;

    private HttpServiceCall httpServiceCall;

    public AssetPlatformService(HttpServiceCall httpServiceCall) {
        this.httpServiceCall = httpServiceCall;
    }

    public Flux<AssetPlatform> getAssetPlatform() {

        log.info("Calling method: getAssetPlatform()", URL_ASSET_PLATFORM);

        return null;
    }

}
