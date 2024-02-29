package ar.com.api.assetPlatform.services

import ar.com.api.assetPlatform.configuration.ExternalServerConfig
import ar.com.api.assetPlatform.configuration.HttpServiceCall
import ar.com.api.assetPlatform.enums.ErrorTypeEnum
import ar.com.api.assetPlatform.exception.ApiServerErrorException
import ar.com.api.assetPlatform.model.AssetPlatform
import org.instancio.Instancio
import org.springframework.http.HttpStatus
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

class AssetPlatformServiceTest extends Specification {

    HttpServiceCall httpServiceCallMock
    ExternalServerConfig externalServerConfigMock
    AssetPlatformService assetPlatformService

    def setup() {
        httpServiceCallMock = Mock(HttpServiceCall)
        externalServerConfigMock = Mock(ExternalServerConfig)
        externalServerConfigMock.getUrlAssertPlatform() >> "mockAssetPlatformURL"
        assetPlatformService = new AssetPlatformService(httpServiceCallMock, externalServerConfigMock)
    }

    def "getAssetPlatform method returns expected Flux of AssetPlatform"() {
        given: "A mock setup for HttpServiceCall and ExternalServerConfig"
        def expectedAssetPlatform = Instancio.ofList(AssetPlatform.class)
                .size(4).create()
        httpServiceCallMock.getFluxObject("mockAssetPlatformURL", AssetPlatform.class) >>
                Flux.fromIterable(expectedAssetPlatform)

        when: "getAssertPlatform is called"
        def actualObject = assetPlatformService.getAssetPlatform()

        then: "Teh result should match the expected Flux of AssetPlatform"
        StepVerifier.create(actualObject)
                .recordWith(ArrayList::new)
                .thenConsumeWhile({item -> true})
                .consumeRecordedWith({actualList ->
                    then: "Validate the collected items"
                    assert actualList.size() == expectedAssetPlatform.size()
                    assert actualList.containsAll(expectedAssetPlatform)
                })
                .verifyComplete()
    }

    def "getAssetPlatform should handle 4xx client error gracefully"() {
        given: "A mock setup for HttpServiceCall and ExternalServerConfig with a 4xx client error"
        def clientErrorExpected = new ApiServerErrorException("An error occurred", "Forbidden",
                HttpStatus.FORBIDDEN, ErrorTypeEnum.API_CLIENT_ERROR)
        httpServiceCallMock.getFluxObject("mockAssetPlatformURL", AssetPlatform.class)
                >> Flux.error(clientErrorExpected)

        when: "getAssetPlatform is invoked with a 4xx error scenario"
        def actualExceptionObject = assetPlatformService.getAssetPlatform()

        then: "The service gracefully handle 4xx client error gracefully"
        StepVerifier
                .create(actualExceptionObject)
                .expectErrorMatches {throwable ->  {
                    throwable instanceof ApiServerErrorException &&
                            throwable.getErrorType() == ErrorTypeEnum.API_CLIENT_ERROR &&
                            throwable.getHttpStatus().is4xxClientError()
                }}
                .verify()
    }

    def "getAssetPlatform should handle 5xx client error gracefully"() {
        given: "A mock setup for HttpServiceCall and ExternalServerConfig with a 5xx client error"
        def clientError = new ApiServerErrorException("Server error", "Not Extended",
                HttpStatus.NOT_EXTENDED, ErrorTypeEnum.API_SERVER_ERROR)
        httpServiceCallMock.getFluxObject("mockAssetPlatformURL", AssetPlatform.class)
                >> Flux.error(clientError)

        when: "getAssetPlatform is invoked with 5xx error scenario"
        def actualExceptionObject = assetPlatformService.getAssetPlatform()

        then: "The service gracefully handle 5xx client error gracefully"
        StepVerifier.create(actualExceptionObject)
                .expectErrorMatches {throwable -> {
                    throwable instanceof ApiServerErrorException &&
                            throwable.getErrorType() == ErrorTypeEnum.API_SERVER_ERROR &&
                            throwable.getHttpStatus().is5xxServerError()
                }}
                .verify()
    }

}
