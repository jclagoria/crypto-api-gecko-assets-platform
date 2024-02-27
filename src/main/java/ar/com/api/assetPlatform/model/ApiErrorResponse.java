package ar.com.api.assetPlatform.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiErrorResponse {

    private final int code;
    private final String message;
    private final String errorMessage;

}
