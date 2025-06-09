package com.backend.study_hub_api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCountDTO {
    private Long pending;
    private Long confirmed;
    private Long shipping;
    private Long shippingFeeUpdated;
    private Long delivered;
    private Long completed;
    private Long cancelled;
}