package com.vladima.gamingrental.client.exceptions;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class ClientException extends RuntimeException{
    private final String template = "Error fetching client with {0}";
    private final String exceptionBody;

    public ClientException(String info) {
        super("Client exception");
        this.exceptionBody = MessageFormat.format(template, info);
    }
}
