package ar.com.api.assetPlatform.handler;

import ar.com.api.assetPlatform.exception.ApiClientErrorException;
import ar.com.api.assetPlatform.model.Ping;
import ar.com.api.assetPlatform.services.CoinGeckoServiceStatus;
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
public class HealthCoinGeckoApiHandler {

    private CoinGeckoServiceStatus serviceStatus;

    public Mono<ServerResponse> getStatusServiceCoinGecko(ServerRequest serverRequest) {

        log.info("In getStatusServiceCoinGecko");

        return serviceStatus.getStatusCoinGeckoService()
                .flatMap(ping -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(ping))
                .doOnSubscribe(subscription -> log.info("Retrieving status of Gecko Service"))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(error ->
                        Mono.error(new
                                ApiClientErrorException("An expected error occurred in getStatusCoinGeckoService",
                                HttpStatus.INTERNAL_SERVER_ERROR))
                );
    }

}
