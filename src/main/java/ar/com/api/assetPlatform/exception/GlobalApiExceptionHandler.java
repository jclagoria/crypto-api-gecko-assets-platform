package ar.com.api.assetPlatform.exception;

import ar.com.api.assetPlatform.model.ApiErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler {

    @ExceptionHandler(ApiClientErrorException.class)
    public Mono<Void> handleClientErrorRequestException(ServerWebExchange serverWebExchange,
                                                        ApiClientErrorException apiClientErrorException)
            throws JsonProcessingException {

        log.error("A ApiClientErrorException occurred", apiClientErrorException);

        ServerHttpResponse response = serverWebExchange.getResponse();
        HttpStatus httpStatus = apiClientErrorException.getHttpStatus();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiErrorResponse apiError = ApiErrorResponse
                .builder()
                .code(httpStatus.value())
                .message(apiClientErrorException.getMessage())
                .build();

        return response.writeWith(
                Mono.just(response.bufferFactory()
                        .wrap(new ObjectMapper().writeValueAsBytes(apiError))));
    }

    @ExceptionHandler(ApiServerErrorException.class)
    public Mono<Void> handleServerErrorRequestException(ApiServerErrorException apiServerErrorException,
                                                        ServerWebExchange serverWebExchange)
            throws JsonProcessingException {

        log.error("An ApiServerErrorException", apiServerErrorException);

        ServerHttpResponse response = serverWebExchange.getResponse();
        HttpStatus httpStatus = apiServerErrorException.getHttpStatus();

        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiErrorResponse apiErrorResponse = ApiErrorResponse
                .builder()
                .code(httpStatus.value())
                .message(apiServerErrorException.getMessage())
                .errorMessage(apiServerErrorException.getOriginalMessage())
                .build();

        return response.writeWith(
                Mono.just(response.bufferFactory()
                        .wrap(new ObjectMapper().writeValueAsBytes(apiErrorResponse))));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ServerResponse> handleGeneralException(Exception ex, ServerWebExchange serverWebExchange) {

        log.error("An unexpected Exception occurred", ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Unexpected Internal Server Error.");
        body.put("message", "An unexpected error has occurred.");
        body.put("pathError", serverWebExchange.getRequest().getPath());
        body.put("stackTrace", ex.getStackTrace());

        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(body);
    }

}
