package com.vladima.gamingrental.helpers;

import org.springframework.data.domain.Sort;

public enum SortDirection {

    asc, desc;

    public Sort by(String... field) {
        var sort = Sort.by(field);
        if (this == desc) {
            sort = sort.descending();
        }
        return sort;
    }
}
