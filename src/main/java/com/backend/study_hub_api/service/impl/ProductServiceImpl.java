package com.backend.study_hub_api.service.impl;

import com.backend.study_hub_api.dto.CategoryDTO;
import com.backend.study_hub_api.dto.ProductDTO;
import com.backend.study_hub_api.dto.UserDTO;
import com.backend.study_hub_api.dto.common.PaginationDTO;
import com.backend.study_hub_api.dto.criteria.ProductFilterCriteria;
import com.backend.study_hub_api.helper.enumeration.ProductStatus;
import com.backend.study_hub_api.helper.exception.BadRequestException;
import com.backend.study_hub_api.helper.util.PaginationUtils;
import com.backend.study_hub_api.model.Category;
import com.backend.study_hub_api.model.Product;
import com.backend.study_hub_api.model.ProductImage;
import com.backend.study_hub_api.model.User;
import com.backend.study_hub_api.repository.ProductRepository;
import com.backend.study_hub_api.service.CategoryService;
import com.backend.study_hub_api.service.FileUploadService;
import com.backend.study_hub_api.service.ProductService;
import com.backend.study_hub_api.service.UserService;
import com.backend.study_hub_api.specification.ProductSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.backend.study_hub_api.helper.constant.Message.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    CategoryService categoryService;
    UserService userService;
    FileUploadService fileUploadService;
    ProductSpecification productSpecification;

    private static final String[] ALLOWED_IMAGE_TYPES = {
        "image/jpeg", "image/jpg", "image/png"
    };

    private static final long MAX_FILE_SIZE_MB = 10;
    private static final int MAX_IMAGES_COUNT = 5;

    @Override
    @Transactional
    public ProductDTO.ProductResponse createProduct(ProductDTO.CreateProductRequest request) {
        User seller = userService.getCurrentUser();
        Category category = categoryService.getCategoryByIdOrThrow(request.getCategoryId());

        validateImageFiles(request.getImages());

        Product product = Product.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .condition(request.getCondition())
                .address(request.getAddress())
                .deliveryMethod(request.getDeliveryMethod())
                .seller(seller)
                .category(category)
                .viewCount(0)
                .build();

        product = productRepository.save(product);

        if (!CollectionUtils.isEmpty(request.getImages())) {
            List<ProductImage> images = new ArrayList<>();
            boolean isFirst = true;

            for (MultipartFile imageFile : request.getImages()) {
                if (!imageFile.isEmpty()) {
                    String imageUrl = fileUploadService.uploadFile(
                        imageFile,
                        "products/images",
                        ALLOWED_IMAGE_TYPES
                    ).getFileUrl();

                    ProductImage image = ProductImage.builder()
                            .imageUrl(imageUrl)
                            .isPrimary(isFirst)
                            .product(product)
                            .build();
                    images.add(image);
                    isFirst = false;
                }
            }

            product.setImages(images);
        }

        product = productRepository.save(product);

        return mapToProductResponse(product);
    }

    @Override
    @Transactional
    public ProductDTO.ProductResponse getProductById(Long id) {
        Product product = getProductByIdOrThrow(id);

        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);

        return mapToProductResponse(product);
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        Product product = getProductByIdOrThrow(id);
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    @Override
    public Product getProductByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(PRODUCT_NOT_FOUND_ERROR));
    }

    @Override
    public List<ProductDTO.ProductSummaryResponse> getAllProducts() {
        return productRepository.findAll()
                                .stream()
                                .map(this::mapToProductSummaryResponse)
                                .toList();
    }

    @Override
    public PaginationDTO<ProductDTO.ProductSummaryResponse> getProductsWithFilter(ProductFilterCriteria criteria) {
        Specification<Product> specification = productSpecification.build(criteria);

        Sort.Direction direction = Sort.Direction.fromString(criteria.getSortDirection());
        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                Sort.by(direction, criteria.getSortBy())
        );

        Page<Product> productPage = productRepository.findAll(specification, pageable);

        Page<ProductDTO.ProductSummaryResponse> responsePage = productPage.map(this::mapToProductSummaryResponse);

        return PaginationUtils.createPaginationResponse(responsePage);
    }

    @Override
    public PaginationDTO<ProductDTO.ProductSummaryResponse> getAllProducts(Pageable pageable) {
        return getProductsWithFilter(ProductFilterCriteria.builder()
                                                          .statuses(List.of(ProductStatus.AVAILABLE))
                                                          .page(pageable.getPageNumber())
                                                          .size(pageable.getPageSize())
                                                          .build());
    }

    @Override
    public ProductDTO.ProductResponse mapToProductResponse(Product product) {
        return ProductDTO.ProductResponse.builder()
                                         .id(product.getId())
                                         .title(product.getTitle())
                                         .description(product.getDescription())
                                         .price(product.getPrice())
                                         .viewCount(product.getViewCount())
                                         .condition(product.getCondition())
                                         .status(product.getStatus())
                                         .address(product.getAddress())
                                         .deliveryMethod(product.getDeliveryMethod())
                                         .seller(userService.mapToDTO(product.getSeller()))
                                         .category(categoryService.mapToDTO(product.getCategory()))
                                         .images(mapToProductImageDTOs(product.getImages()))
                                         .primaryImageUrl(product.getPrimaryImageUrl())
                                         .createdAt(product.getCreatedAt())
                                         .updatedAt(product.getUpdatedAt())
                                         .build();
    }

    @Override
    public List<ProductDTO.ProductSummaryResponse> getTop10Products() {
        return productRepository.findByStatusOrderByViewCountDesc(ProductStatus.AVAILABLE)
                                .stream()
                                .map(this::mapToProductSummaryResponse)
                                .limit(10)
                                .toList();
    }

    private void validateImageFiles(List<MultipartFile> images) {
        if (CollectionUtils.isEmpty(images)) {
            throw new BadRequestException(PRODUCT_IMAGE_REQUIRED_ERROR);
        }

        if (images.size() > MAX_IMAGES_COUNT) {
            throw new BadRequestException(PRODUCT_IMAGE_MAX_COUNT_ERROR);
        }

        for (MultipartFile file : images) {
            if (file.isEmpty()) {
                continue;
            }

            // Validate file type
            if (!isValidImageType(file)) {
                throw new BadRequestException(PRODUCT_IMAGE_TYPE_NOT_SUPPORTED_ERROR);
            }

            // Validate file size
            if (file.getSize() > MAX_FILE_SIZE_MB * 1024L * 1024L) {
                throw new BadRequestException(PRODUCT_IMAGE_MAX_SIZE_ERROR);
            }
        }
    }

    private boolean isValidImageType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null &&
                Arrays.stream(ALLOWED_IMAGE_TYPES)
                        .anyMatch(type -> type.equalsIgnoreCase(contentType));
    }

    private ProductDTO.ProductSummaryResponse mapToProductSummaryResponse(Product product) {
        return ProductDTO.ProductSummaryResponse.builder()
                                         .id(product.getId())
                                         .title(product.getTitle())
                                         .price(product.getPrice())
                                         .viewCount(product.getViewCount())
                                         .condition(product.getCondition())
                                         .status(product.getStatus())
                                         .seller(userService.mapToDTO(product.getSeller()))
                                         .primaryImageUrl(product.getPrimaryImageUrl())
                                         .createdAt(product.getCreatedAt())
                                         .categoryName(product.getCategory().getName())
                                         .build();
    }

    private List<ProductDTO.ProductImageDTO> mapToProductImageDTOs(List<ProductImage> images) {
        if (CollectionUtils.isEmpty(images)) {
            return new ArrayList<>();
        }

        return images.stream()
                .map(image -> ProductDTO.ProductImageDTO.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .isPrimary(image.getIsPrimary())
                        .createdAt(image.getCreatedAt())
                        .build())
                .toList();
    }
}
