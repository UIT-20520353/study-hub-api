package com.backend.study_hub_api.helper.util;

import com.backend.study_hub_api.dto.common.PaginationDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public class PaginationUtils {

    public static <T> PaginationDTO<T> createPaginationResponse(Page<T> page) {
        return PaginationDTO.<T>builder()
                .items(page.getContent())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageSize(page.getSize())
                .currentPage(page.getNumber())
                .build();
    }
}
