package ar.com.api.assetPlatform.configuration;

import ar.com.api.assetPlatform.enums.ErrorTypeEnum;
import ar.com.api.assetPlatform.exception.ApiServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class HttpServiceCall {

    private final WebClient webClient;

    public HttpServiceCall(WebClient wClient) {
        this.webClient = wClient;
    }

    private <T> Mono<T> handleError(Throwable e) {

        if (e instanceof ApiServerErrorException) {
            return Mono.error(e);
        }

        return Mono.error(new Exception("General Error", e));
    }

    private Mono<ApiServerErrorException> handleResponseError(Map<String, Object> errorMessage,
                                                              HttpStatus status,
                                                              ErrorTypeEnum errorType) {
        String errorBody = (String) errorMessage.getOrDefault("error", "Unknown error");
        return Mono.error(new ApiServerErrorException("Error occurred", errorBody, status, errorType));
    }

    public <T> Mono<T> getMonoObject(String urlEndPoint, Class<T> responseType) {
        return webClient.get()
                .uri(urlEndPoint)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> response.bodyToMono(Map.class)
                                .flatMap(errorMessage -> handleResponseError(errorMessage,
                                        (HttpStatus) response.statusCode(),
                                        ErrorTypeEnum.GECKO_CLIENT_ERROR))
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> response.bodyToMono(Map.class)
                                .flatMap(errorMessage -> handleResponseError(errorMessage,
                                        (HttpStatus) response.statusCode(),
                                        ErrorTypeEnum.GECKO_SERVER_ERROR)))
                .bodyToMono(responseType)
                .onErrorResume(this::handleError);
    }

    public <T> Flux<T> getFluxObject(String urlEndpoint, Class<T> responseType) {
        return webClient.get()
                .uri(urlEndpoint)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        response -> response.bodyToMono(Map.class)
                                .flatMap(errorMessage -> handleResponseError(errorMessage,
                                        (HttpStatus) response.statusCode(),
                                        ErrorTypeEnum.GECKO_CLIENT_ERROR))
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> response.bodyToMono(Map.class)
                                .flatMap(errorMessage -> handleResponseError(errorMessage,
                                        (HttpStatus) response.statusCode(),
                                        ErrorTypeEnum.GECKO_SERVER_ERROR)))
                .bodyToFlux(responseType)
                .onErrorResume(this::handleError);
    }

}
