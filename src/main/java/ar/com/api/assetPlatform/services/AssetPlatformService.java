package ar.com.api.assetPlatform.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.com.api.assetPlatform.model.AssetPlatform;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AssetPlatformService {

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
           .retrieve().bodyToFlux(AssetPlatform.class)
           .doOnError(throwable -> log.error("The service is unavailable!", throwable))
           .onErrorComplete();           
 }

}
