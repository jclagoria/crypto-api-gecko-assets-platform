package ar.com.api.assetPlatform.handler

import ar.com.api.assetPlatform.exception.ApiClientErrorException
import ar.com.api.assetPlatform.model.Ping
import ar.com.api.assetPlatform.services.CoinGeckoServiceStatus
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class HealthCoinGeckoApiHandlerTest extends Specification {

    CoinGeckoServiceStatus serviceStatus
    ServerRequest serverRequest
    HealthCoinGeckoApiHandler handler

    def setup() {
        serviceStatus = Mock(CoinGeckoServiceStatus)
        serverRequest = Mock(ServerRequest)
        handler = new HealthCoinGeckoApiHandler(serviceStatus)
    }

    def "getStatusServiceCoinGecko returns 200 Ok with expected body successfully response"() {
        given: "A mock CoinGeckoServiceStatus and a successfully Ping response"
        def expectedPing = Instancio.create(Ping)
        serviceStatus.getStatusCoinGeckoService() >> Mono.just(expectedPing)

        when: "getStatusServiceCoinGecko is called"
        def actualObject = handler.getStatusServiceCoinGecko(serverRequest)

        then: "The response is 200 Ok with the expected body"
        StepVerifier
                .create(actualObject)
                .expectNextMatches{ ServerResponse response ->
                    response.statusCode() == HttpStatus.OK &&
                            response.headers().getContentType() == MediaType.APPLICATION_JSON }
                .verifyComplete();
    }

    def "getStatusServiceCoinGecko returns 404 Not Found for empty service response"() {
        given: "A mock CoinGecko and an empty response"
        serviceStatus.getStatusCoinGeckoService() >> Mono.empty()

        when: "getStatusServiceCoinGecko is called"
        def actualResult = handler.getStatusServiceCoinGecko(serverRequest)

        then: "The response is 404 Not Found"
        StepVerifier
                .create(actualResult)
                .expectNextMatches {ServerResponse response ->
                    response.statusCode() == HttpStatus.NOT_FOUND
                }.verifyComplete()
    }

    def "getStatusServiceCoinGecko handles errors gracefully"() {
        given: "A mock CoinGeckoServiceStatus and an error response"
        serviceStatus.getStatusCoinGeckoService() >> Mono.error(new RuntimeException("Service failure"))

        when: "getStatusServiceConGecko is called"
        def actualResponse = handler.getStatusServiceCoinGecko(serverRequest)

        then: "The response indicates an internal error"
        StepVerifier
                .create(actualResponse)
                .expectError(ApiClientErrorException.class)
                .verify()
    }

}
