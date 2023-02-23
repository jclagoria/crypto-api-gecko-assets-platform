package ar.com.api.assetPlatform.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ar.com.api.assetPlatform.model.AssetPlatform;
import ar.com.api.assetPlatform.model.Ping;
import ar.com.api.assetPlatform.services.AssetPlatformService;
import ar.com.api.assetPlatform.services.CoinGeckoServiceStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class AssetPlatformApiHandler {
 
 private CoinGeckoServiceStatus serviceStatus;

 private AssetPlatformService serviceAssetPlatform;

 public Mono<ServerResponse> getStatusServiceCoinGecko(ServerRequest serverRequest) {

     log.info("In getStatusServiceCoinGecko");

     return ServerResponse
                    .ok()
                    .body(
                         serviceStatus.getStatusCoinGeckoService(), 
                         Ping.class);
 }

 public Mono<ServerResponse> getAssertPlatformCoinGecko(ServerRequest sRequest){
     log.info("In getAssertPlatformCoinGecko");

     return ServerResponse
                    .ok()
                    .body(
                         serviceAssetPlatform.getAssetPlatform(), 
                         AssetPlatform.class);
 }

}
