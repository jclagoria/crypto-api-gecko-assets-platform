package ar.com.api.assetPlatform.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiCustomException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ApiCustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
