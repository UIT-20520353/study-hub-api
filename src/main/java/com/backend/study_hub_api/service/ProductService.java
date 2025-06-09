package com.backend.study_hub_api.service;

import com.backend.study_hub_api.dto.ProductDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.ProductFilterCriteria;
import com.backend.study_hub_api.model.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductDTO.ProductResponse createProduct(ProductDTO.CreateProductRequest request);

    ProductDTO.ProductResponse getProductById(Long id);

    void deleteProductById(Long id);

    Product getProductByIdOrThrow(Long id);

    List<ProductDTO.ProductSummaryResponse> getAllProducts();

    PaginationDTO<ProductDTO.ProductSummaryResponse> getProductsWithFilter(ProductFilterCriteria criteria);

    PaginationDTO<ProductDTO.ProductSummaryResponse> getAllProducts(Pageable pageable);

    ProductDTO.ProductResponse mapToProductResponse(Product product);

    List<ProductDTO.ProductSummaryResponse> getTop10Products();

}
