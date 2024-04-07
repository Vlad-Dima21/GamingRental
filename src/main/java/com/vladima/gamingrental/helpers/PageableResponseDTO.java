package com.vladima.gamingrental.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageableResponseDTO<Item> {
    int totalPages;
    List<Item> items;
}
