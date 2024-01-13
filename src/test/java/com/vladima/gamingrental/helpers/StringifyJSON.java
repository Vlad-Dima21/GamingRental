package com.vladima.gamingrental.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StringifyJSON {
    public static String toJSON(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
