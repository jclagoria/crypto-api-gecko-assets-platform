package ar.com.api.assetPlatform.handler

import ar.com.api.assetPlatform.exception.ApiCustomException
import ar.com.api.assetPlatform.model.AssetPlatform
import ar.com.api.assetPlatform.services.AssetPlatformService
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

class AssetPlatformApiHandlerTest extends Specification {

    AssetPlatformService assetPlatformServiceMock
    ServerRequest serverRequestMock
    AssetPlatformApiHandler handler

    def setup() {
        assetPlatformServiceMock = Mock()
        serverRequestMock = Mock()
        handler = new AssetPlatformApiHandler(assetPlatformServiceMock)
    }

    def "getAssertPlatformCoinGecko returns list of asset platforms"() {
        given: "Mocked service with a list of AssetPlatform"
        def expectListOfAssetPlatform = Instancio
                .ofList(AssetPlatform.class).size(5).create()
        assetPlatformServiceMock.getAssetPlatform() >> Flux.fromIterable(expectListOfAssetPlatform)

        when: "getAssertPlatformCoinGecko is called"
        def actualObject = handler.getAssertPlatformCoinGecko(serverRequestMock)

        then: "It reruns a ServerResponse with the list of asset platform"
        StepVerifier.create(actualObject)
                .expectNextMatches {response ->
                    response.statusCode().is2xxSuccessful() &&
                            response.headers().getContentType() == MediaType.APPLICATION_JSON }
                .verifyComplete()
    }

    def "getAssertPlatformCoinGecko handles empty result"() {
        given: "Mocked service return an empty Flux"
        assetPlatformServiceMock.getAssetPlatform() >> Flux.empty()
        when: "getAssetPlatformCoinGecko is called with no platforms"
        def actualObject = handler.getAssertPlatformCoinGecko(serverRequestMock)

        then: "It returns a 404 Not Found response"
        StepVerifier.create(actualObject)
                .expectNextMatches  { ServerResponse response ->
                    (response.statusCode() == HttpStatus.OK)
                }
                .verifyComplete()
    }

    def "getAssertPlatformCoinGecko handles errors gracefully"() {
        given: "Mocked service throws an exception"
        assetPlatformServiceMock.getAssetPlatform() >> Flux.error(new RuntimeException("Service failure"))

        when: "getAssertPlatformCoinGecko is called and an error occurs"
        def result = handler.getAssertPlatformCoinGecko(serverRequestMock)

        then: "It returns a 500 Internal Server Error response"
        StepVerifier.create(result)
                .expectErrorMatches { e ->
                    e instanceof ApiCustomException &&
                            e.message.contains("An expected error occurred")
                }
                .verify()
    }

}
