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
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

import static com.backend.study_hub_api.helper.constant.Message.*;

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

        @Schema(description = "View count", example = "25")
        private Integer viewCount;

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

        @Schema(description = "Seller information")
        private UserDTO seller;

        @Schema(description = "View count", example = "25")
        private Integer viewCount;

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
    @Builder
    public static class CreateProductRequest {
        @NotBlank(message = PRODUCT_TITLE_REQUIRED_ERROR)
        @Size(max = 255, message = PRODUCT_TITLE_MAX_LENGTH_ERROR)
        @Schema(description = "Product title", example = "Sách Toán Cao Cấp A1", required = true)
        private String title;

        @Size(max = 2000, message = PRODUCT_DESCRIPTION_MAX_LENGTH_ERROR)
        @Schema(description = "Product description", example = "Sách còn mới 90%, không có ghi chú")
        private String description;

        @NotNull(message = PRODUCT_PRICE_REQUIRED_ERROR)
        @Min(value = 1000, message = PRODUCT_PRICE_MIN_ERROR)
        @Max(value = 50000000, message = PRODUCT_PRICE_MAX_ERROR)
        @Schema(description = "Price in VND", example = "150000", required = true)
        private Integer price;

        @NotNull(message = PRODUCT_CONDITION_REQUIRED_ERROR)
        @Schema(description = "Product condition", required = true)
        private ProductCondition condition;

        @Schema(description = "Seller address", example = "Quận 1, TP.HCM")
        private String address;

        @Schema(description = "Delivery method", example = "BOTH")
        private DeliveryMethod deliveryMethod;

        @NotNull(message = PRODUCT_CATEGORY_REQUIRED_ERROR)
        @Schema(description = "Category ID", example = "1", required = true)
        private Long categoryId;

        @Schema(description = "Product images as multipart files")
        private List<MultipartFile> images;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Update product request")
    public static class UpdateProductRequest {
        @Size(max = 255, message = PRODUCT_TITLE_MAX_LENGTH_ERROR)
        @Schema(description = "Product title", example = "Sách Toán Cao Cấp A1 (Đã cập nhật)")
        private String title;

        @Size(max = 2000, message = PRODUCT_DESCRIPTION_MAX_LENGTH_ERROR)
        @Schema(description = "Product description")
        private String description;

        @Min(value = 1000, message = PRODUCT_PRICE_MIN_ERROR)
        @Max(value = 50000000, message = PRODUCT_PRICE_MAX_ERROR)
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