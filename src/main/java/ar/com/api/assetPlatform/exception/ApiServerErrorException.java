package ar.com.api.assetPlatform.exception;

import ar.com.api.assetPlatform.enums.ErrorTypeEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiServerErrorException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String originalMessage;
    private final ErrorTypeEnum errorType;

    public ApiServerErrorException(String message, String originalMessage,
                                   HttpStatus httpStatus, ErrorTypeEnum errorType) {
        super(message);
        this.httpStatus = httpStatus;
        this.originalMessage = originalMessage;
        this.errorType = errorType;
    }

}
