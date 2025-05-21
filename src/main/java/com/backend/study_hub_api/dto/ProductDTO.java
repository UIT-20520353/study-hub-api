package com.backend.study_hub_api.dto;

import com.backend.study_hub_api.helper.enumeration.DeliveryMethod;
import com.backend.study_hub_api.helper.enumeration.ProductCondition;
import com.backend.study_hub_api.helper.enumeration.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

public class ProductDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Product response")
    public static class ProductResponse {
        @Schema(description = "Product ID", example = "1")
        private Long id;

        @Schema(description = "Product title", example = "Sách Toán Cao Cấp")
        private String title;

        @Schema(description = "Product description")
        private String description;

        @Schema(description = "Price in VND", example = "150000")
        private Integer price;

        @Schema(description = "Product condition")
        private ProductCondition condition;

        @Schema(description = "Product status")
        private ProductStatus status;

        @Schema(description = "Seller address")
        private String address;

        @Schema(description = "Delivery method")
        private DeliveryMethod deliveryMethod;

        @Schema(description = "Seller information")
        private UserDTO seller;

        @Schema(description = "Category information")
        private CategoryDTO.CategoryResponse category;

        @Schema(description = "Product images")
        private List<ProductImageDTO> images;

        @Schema(description = "Primary image URL")
        private String primaryImageUrl;

        @Schema(description = "Creation time")
        private Instant createdAt;

        @Schema(description = "Last update time")
        private Instant updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Product summary for list view")
    public static class ProductSummaryResponse {
        @Schema(description = "Product ID", example = "1")
        private Long id;

        @Schema(description = "Product title", example = "Sách Toán Cao Cấp")
        private String title;

        @Schema(description = "Price in VND", example = "150000")
        private Integer price;

        @Schema(description = "Product condition")
        private ProductCondition condition;

        @Schema(description = "Product status")
        private ProductStatus status;

        @Schema(description = "Primary image URL")
        private String primaryImageUrl;

        @Schema(description = "Seller name")
        private String sellerName;

        @Schema(description = "Category name")
        private String categoryName;

        @Schema(description = "Creation time")
        private Instant createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Create product request")
    public static class CreateProductRequest {
        @NotBlank(message = "Tiêu đề sản phẩm không được để trống")
        @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
        @Schema(description = "Product title", example = "Sách Toán Cao Cấp A1", required = true)
        private String title;

        @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
        @Schema(description = "Product description", example = "Sách còn mới 90%, không có ghi chú")
        private String description;

        @NotNull(message = "Giá sản phẩm không được để trống")
        @Min(value = 1000, message = "Giá tối thiểu là 1,000 VND")
        @Max(value = 50000000, message = "Giá tối đa là 50,000,000 VND")
        @Schema(description = "Price in VND", example = "150000", required = true)
        private Integer price;

        @NotNull(message = "Tình trạng sản phẩm không được để trống")
        @Schema(description = "Product condition", required = true)
        private ProductCondition condition;

        @Schema(description = "Seller address", example = "Quận 1, TP.HCM")
        private String address;

        @Schema(description = "Delivery method", example = "BOTH")
        private DeliveryMethod deliveryMethod;

        @NotNull(message = "Danh mục không được để trống")
        @Schema(description = "Category ID", example = "1", required = true)
        private Long categoryId;

        @Schema(description = "Product image URLs")
        private List<String> imageUrls;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Update product request")
    public static class UpdateProductRequest {
        @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
        @Schema(description = "Product title", example = "Sách Toán Cao Cấp A1 (Đã cập nhật)")
        private String title;

        @Size(max = 2000, message = "Mô tả không được vượt quá 2000 ký tự")
        @Schema(description = "Product description")
        private String description;

        @Min(value = 1000, message = "Giá tối thiểu là 1,000 VND")
        @Max(value = 50000000, message = "Giá tối đa là 50,000,000 VND")
        @Schema(description = "Price in VND", example = "140000")
        private Integer price;

        @Schema(description = "Product condition")
        private ProductCondition condition;

        @Schema(description = "Product status")
        private ProductStatus status;

        @Schema(description = "Seller address")
        private String address;

        @Schema(description = "Delivery method")
        private DeliveryMethod deliveryMethod;

        @Schema(description = "Category ID", example = "1")
        private Long categoryId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Product image")
    public static class ProductImageDTO {
        @Schema(description = "Image ID")
        private Long id;

        @Schema(description = "Image URL")
        private String imageUrl;

        @Schema(description = "Is primary image")
        private Boolean isPrimary;

        @Schema(description = "Creation time")
        private Instant createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Product search request")
    public static class ProductSearchRequest {
        @Schema(description = "Search keyword")
        private String keyword;

        @Schema(description = "Category ID")
        private Long categoryId;

        @Schema(description = "Minimum price")
        private Integer minPrice;

        @Schema(description = "Maximum price")
        private Integer maxPrice;

        @Schema(description = "Product condition")
        private ProductCondition condition;

        @Schema(description = "Product status")
        private ProductStatus status;

        @Schema(description = "Seller university")
        private String university;

        @Schema(description = "Delivery method")
        private DeliveryMethod deliveryMethod;

        @Schema(description = "Sort by field (price, createdAt, title)")
        private String sortBy = "createdAt";

        @Schema(description = "Sort direction (asc, desc)")
        private String sortDirection = "desc";

        @Schema(description = "Page number (0-based)")
        private Integer page = 0;

        @Schema(description = "Page size")
        private Integer size = 20;
    }
}