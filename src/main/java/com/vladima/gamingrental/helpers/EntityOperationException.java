package com.vladima.gamingrental.helpers;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@EqualsAndHashCode(callSuper = false)
public class EntityOperationException extends RuntimeException {
    private final String extraInfo;
    private final String fieldName;
    private final HttpStatus status;

    public EntityOperationException(String message, String info, HttpStatus status) {
        super(message);
        this.extraInfo = info;
        this.status = status;
        this.fieldName = null;
    }

    public EntityOperationException(String message, String extraInfo, String fieldName, HttpStatus status) {
        super(message);
        this.extraInfo = extraInfo;
        this.fieldName = fieldName;
        this.status = status;
    }
}
