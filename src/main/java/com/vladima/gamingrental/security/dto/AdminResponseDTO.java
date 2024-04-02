package com.vladima.gamingrental.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class AdminResponseDTO {
    private String token;
}
