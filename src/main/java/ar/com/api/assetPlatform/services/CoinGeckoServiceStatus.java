package ar.com.api.assetPlatform.services;

import ar.com.api.assetPlatform.configuration.HttpServiceCall;
import ar.com.api.assetPlatform.model.Ping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoServiceStatus extends CoinGeckoServiceApi {

    private final HttpServiceCall httpServiceCall;
    @Value("${api.ping}")
    private String URL_PING_SERVICE;

    public CoinGeckoServiceStatus(HttpServiceCall serviceCall) {
        this.httpServiceCall = serviceCall;
    }

    public Mono<Ping> getStatusCoinGeckoService() {

        log.info("Calling method: ", URL_PING_SERVICE);

        return httpServiceCall.getMonoObject(URL_PING_SERVICE, Ping.class);
    }

}
