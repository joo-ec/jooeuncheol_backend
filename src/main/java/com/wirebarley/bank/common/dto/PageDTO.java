package com.wirebarley.bank.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@AllArgsConstructor
public class PageDTO {
    private boolean first;
    private boolean last;

    private int currentPage;

    private int pageSize;
    private long totalElements;
    private int totalPages;

    public PageDTO(Page page) {
        first = page.isFirst();
        last = page.isLast();
        currentPage = page.getNumber();
        pageSize = page.getSize();
        totalElements = page.getTotalElements();
        totalPages = page.getTotalPages();
    }
}
