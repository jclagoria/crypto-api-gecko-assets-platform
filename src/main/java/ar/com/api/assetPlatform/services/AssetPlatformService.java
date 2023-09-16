package ar.com.api.assetPlatform.services;

import ar.com.api.assetPlatform.exception.ManageExceptionCoinGeckoServiceApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.com.api.assetPlatform.model.AssetPlatform;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AssetPlatformService extends CoinGeckoServiceApi {

 @Value("${api.urlAssertPlatform}")
 private String URL_ASSET_PLATFORM;
 
 private WebClient wClient;

 public AssetPlatformService(WebClient webClient) {
  this.wClient = webClient;
 }

 public Flux<AssetPlatform> getAssetPlatform(){

  log.info("Calling method: getAssetPlatform()", URL_ASSET_PLATFORM); 
  
  return wClient
          .get()
          .uri(URL_ASSET_PLATFORM)
          .retrieve()
          .onStatus(
                  HttpStatusCode::is4xxClientError,
                  getClientResponseMonoDataException()
          )
          .onStatus(
                  HttpStatusCode::is5xxServerError,
                  getClientResponseMonoServerException()
          )
          .bodyToFlux(AssetPlatform.class)
          .doOnError(
                  ManageExceptionCoinGeckoServiceApi::throwServiceException
          );
 }

}
