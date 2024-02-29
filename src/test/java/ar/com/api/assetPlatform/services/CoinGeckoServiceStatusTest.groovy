package ar.com.api.assetPlatform.services

import ar.com.api.assetPlatform.configuration.ExternalServerConfig
import ar.com.api.assetPlatform.configuration.HttpServiceCall
import ar.com.api.assetPlatform.enums.ErrorTypeEnum
import ar.com.api.assetPlatform.exception.ApiServerErrorException
import ar.com.api.assetPlatform.model.Ping
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class CoinGeckoServiceStatusTest extends Specification {

    HttpServiceCall httpServiceCall
    ExternalServerConfig externalServerConfig
    CoinGeckoServiceStatus service

    def setup() {
        httpServiceCall = Mock(HttpServiceCall)
        externalServerConfig = Mock(ExternalServerConfig)
        externalServerConfig.getPing() >> "mockPingURL"
        service = new CoinGeckoServiceStatus(httpServiceCall, externalServerConfig)
    }

    def "CoinGeckoServiceStatus should successfully retrieve service status"() {
        given: "A mock setup for HttpServiceCall and ExternalServerConfig"
        def expectedPing = Instancio.create(Ping)
        httpServiceCall.getMonoObject("mockPingURL", Ping.class) >> Mono.just(expectedPing)

        when: "getStatusCoinGeckoService is invoked"
        def actualObject = service.getStatusCoinGeckoService()

        then: "The service returns the expected Ping object"
        StepVerifier.create(actualObject)
                .expectNext(expectedPing)
                .verifyComplete()
    }

    def "CoinGeckoService should handle 4xx client error gracefully"() {
        given: "A mock setup for HttServiceCall and ExternalServerConfig with a 4xx client error"
        def clientError = new ApiServerErrorException("An error occurred", "Bad Request",
                HttpStatus.BAD_REQUEST, ErrorTypeEnum.API_CLIENT_ERROR)
        httpServiceCall.getMonoObject("mockPingURL", Ping.class) >> Mono.error(clientError)

        when: "getStatusCoinGeckoService is invoked with a 4xx error scenario"
        def actualObject = service.getStatusCoinGeckoService()

        then: "The service gracefully handles the error"
        StepVerifier.create(actualObject)
                .expectErrorMatches(throwable -> {
                    throwable instanceof ApiServerErrorException &&
                            throwable.getErrorType() == ErrorTypeEnum.API_CLIENT_ERROR &&
                            throwable.getHttpStatus().is4xxClientError()
                }).verify()
    }

    def "CoinGeckoService should handle 5xx client error gracefully"() {
        given: "A mock setup for HttServiceCall and ExternalServerConfig with a 5xx client error"
        def clientError = new ApiServerErrorException("Server error", "Not Implemented",
                HttpStatus.NOT_IMPLEMENTED, ErrorTypeEnum.API_SERVER_ERROR)
        httpServiceCall.getMonoObject("mockPingURL", Ping.class) >> Mono.error(clientError)

        when: "getStatusCoinGeckoService is invoked with a 4xx error scenario"
        def actualObject = service.getStatusCoinGeckoService()

        then: "The service gracefully handles the error"
        StepVerifier.create(actualObject)
                .expectErrorMatches(throwable -> {
                    throwable instanceof ApiServerErrorException &&
                            throwable.getErrorType() == ErrorTypeEnum.API_SERVER_ERROR &&
                            throwable.getHttpStatus().is5xxServerError()
                }).verify()
    }

}
