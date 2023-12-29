package com.vladima.gamingrental.helpers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public class EntityOperationException extends RuntimeException {
    private final String extraInfo;
    private final HttpStatus status;

    public EntityOperationException(String message, String info, HttpStatus status) {
        super(message);
        this.extraInfo = info;
        this.status = status;
    }
}
