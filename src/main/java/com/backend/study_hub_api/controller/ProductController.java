package com.backend.study_hub_api.controller;

import com.backend.study_hub_api.dto.ProductDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.ProductFilterCriteria;
import com.backend.study_hub_api.helper.enumeration.DeliveryMethod;
import com.backend.study_hub_api.helper.enumeration.ProductCondition;
import com.backend.study_hub_api.helper.enumeration.ProductStatus;
import com.backend.study_hub_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user/products")
@RequiredArgsConstructor
@Tag(name = "Public Products", description = "Endpoints for products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    
    ProductService productService;

    @PostMapping("/create")
    @Operation(summary = "Create product with image URLs", description = "Create a new product with image URLs")
    public ResponseEntity<ProductDTO.ProductResponse> createProductWithFiles(
            @Parameter(description = "Product title", required = true)
            @RequestParam("title") String title,
            
            @Parameter(description = "Product description")
            @RequestParam(value = "description", required = false) String description,
            
            @Parameter(description = "Price in VND", required = true)
            @RequestParam("price") Integer price,
            
            @Parameter(description = "Product condition", required = true)
            @RequestParam("condition") ProductCondition condition,
            
            @Parameter(description = "Seller address")
            @RequestParam(value = "address", required = false) String address,
            
            @Parameter(description = "Delivery method")
            @RequestParam(value = "deliveryMethod", required = false) DeliveryMethod deliveryMethod,
            
            @Parameter(description = "Category ID", required = true)
            @RequestParam("categoryId") Long categoryId,
            
            @Parameter(description = "Product images (max 5 files, 10MB each)")
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        ProductDTO.CreateProductRequest request = ProductDTO.CreateProductRequest.builder()
                .title(title)
                .description(description)
                .price(price)
                .condition(condition)
                .address(address)
                .deliveryMethod(deliveryMethod)
                .categoryId(categoryId)
                .images(images)
                .build();

        ProductDTO.ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID and increment view count")
    public ResponseEntity<ProductDTO.ProductResponse> getProductById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        ProductDTO.ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by ID", description = "Delete a specific product by its ID")
    public ResponseEntity<Void> deleteProductById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    public ResponseEntity<List<ProductDTO.ProductSummaryResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/top-10")
    @Operation(summary = "Get top 10 products", description = "Retrieve the top 10 products based on view count")
    public ResponseEntity<List<ProductDTO.ProductSummaryResponse>> getTop10Products() {
        List<ProductDTO.ProductSummaryResponse> topProducts = productService.getTop10Products();
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products with filters", description = "Search and filter products with pagination")
    public ResponseEntity<PaginationDTO<ProductDTO.ProductSummaryResponse>> searchProducts(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String searchKeyword,
            @Parameter(description = "Product title") @RequestParam(required = false) String title,
            @Parameter(description = "Product description") @RequestParam(required = false) String description,
            @Parameter(description = "Seller ID") @RequestParam(required = false) Long sellerId,
            @Parameter(description = "Seller name") @RequestParam(required = false) String sellerName,
            @Parameter(description = "Category IDs") @RequestParam(required = false) List<Long> categoryIds,
            @Parameter(description = "Category name") @RequestParam(required = false) String categoryName,
            @Parameter(description = "University ID") @RequestParam(required = false) Long universityId,
            @Parameter(description = "University name") @RequestParam(required = false) String universityName,
            @Parameter(description = "Product statuses") @RequestParam(required = false) List<ProductStatus> statuses,
            @Parameter(description = "Product conditions") @RequestParam(required = false) List<ProductCondition> conditions,
            @Parameter(description = "Delivery methods") @RequestParam(required = false) List<DeliveryMethod> deliveryMethods,
            @Parameter(description = "Minimum price") @RequestParam(required = false) Integer minPrice,
            @Parameter(description = "Maximum price") @RequestParam(required = false) Integer maxPrice,
            @Parameter(description = "Minimum view count") @RequestParam(required = false) Integer minViewCount,
            @Parameter(description = "Maximum view count") @RequestParam(required = false) Integer maxViewCount,
            @Parameter(description = "Address") @RequestParam(required = false) String address,
            @Parameter(description = "Created from (ISO format)") @RequestParam(required = false) String createdFrom,
            @Parameter(description = "Created to (ISO format)") @RequestParam(required = false) String createdTo,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        ProductFilterCriteria criteria = ProductFilterCriteria.builder()
                                                              .searchKeyword(searchKeyword)
                                                              .title(title)
                                                              .description(description)
                                                              .sellerId(sellerId)
                                                              .sellerName(sellerName)
                                                              .categoryIds(categoryIds)
                                                              .categoryName(categoryName)
                                                              .universityId(universityId)
                                                              .universityName(universityName)
                                                              .statuses(statuses)
                                                              .conditions(conditions)
                                                              .deliveryMethods(deliveryMethods)
                                                              .minPrice(minPrice)
                                                              .maxPrice(maxPrice)
                                                              .minViewCount(minViewCount)
                                                              .maxViewCount(maxViewCount)
                                                              .address(address)
                                                              .createdFrom(createdFrom)
                                                              .createdTo(createdTo)
                                                              .page(page)
                                                              .size(size)
                                                              .sortBy(sortBy)
                                                              .sortDirection(sortDirection)
                                                              .build();

        PaginationDTO<ProductDTO.ProductSummaryResponse> response = productService.getProductsWithFilter(criteria);
        return ResponseEntity.ok(response);
    }
}
