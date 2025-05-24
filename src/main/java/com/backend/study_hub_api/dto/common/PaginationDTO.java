package com.backend.study_hub_api.dto.common;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@Data
public class PaginationDTO<T> {

    private int totalPages;
    private long totalElements;
    private List<T> items;
    private int pageSize;
    private int currentPage;

}
