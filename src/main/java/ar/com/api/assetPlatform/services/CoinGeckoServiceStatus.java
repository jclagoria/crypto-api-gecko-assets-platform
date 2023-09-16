package ar.com.api.assetPlatform.services;

import ar.com.api.assetPlatform.exception.ManageExceptionCoinGeckoServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.com.api.assetPlatform.model.Ping;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CoinGeckoServiceStatus extends CoinGeckoServiceApi {
 
 @Value("${api.ping}")
 private String URL_PING_SERVICE;

 private WebClient webClient;

 public CoinGeckoServiceStatus(WebClient webClient) {
  this.webClient = webClient;
 }

 public Mono<Ping> getStatusCoinGeckoService() {
  
  log.info("Calling method: ", URL_PING_SERVICE); 

  return webClient
          .get()
          .uri(URL_PING_SERVICE)
          .retrieve()
          .onStatus(
                  HttpStatusCode::is4xxClientError,
                  getClientResponseMonoDataException()
          )
          .onStatus(
                  HttpStatusCode::is5xxServerError,
                  getClientResponseMonoServerException()
          )
          .bodyToMono(Ping.class)
          .doOnError(
                  ManageExceptionCoinGeckoServiceApi::throwServiceException
          );
 }

}
