package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.ProductDetailRequest;
import com.example.BadmintonShop.DTO.Response.PaginationResponse;
import com.example.BadmintonShop.DTO.Response.ProductDetailResponseDTO;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.ProductDetailMapper;
import com.example.BadmintonShop.Model.Brand;
import com.example.BadmintonShop.Model.Category;
import com.example.BadmintonShop.Model.Product;
import com.example.BadmintonShop.Model.ProductDetail;
import com.example.BadmintonShop.Repository.*;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ImageRepository imageRepository;
    private final ProductDetailMapper productDetailMapper;

    @Transactional(readOnly = true)
    public PaginationResponse<ProductDetailResponseDTO> getAllProducts(Pageable pageable){
        // 1. Gọi query đã tối ưu (JOIN FETCH)
        Page<ProductDetail> productDetailPage = productDetailRepository.findAllWithDetail(pageable);

        // 2. Chuyển đổi Page<Entity> sang Page<DTO>
        Page<ProductDetailResponseDTO> dtoPage = productDetailPage
                .map(productDetailMapper::toProductDetailDTO);

        // 3. Chuyển Page<DTO> sang DTO phân trang tùy chỉnh của bạn
        return PaginationResponse.fromPage(dtoPage);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ProductDetailResponseDTO> searchProductsByName(
            String keyword, Pageable pageable) {

        // 1. Gọi query tìm kiếm
        Page<ProductDetail> productDetailPage = productDetailRepository.findByProductNameContaining(keyword, pageable);

        // 2. Chuyển đổi Page<Entity> sang Page<DTO>
        Page<ProductDetailResponseDTO> dtoPage = productDetailPage
                .map(productDetailMapper::toProductDetailDTO);

        // 3. Chuyển Page<DTO> sang DTO phân trang
        return PaginationResponse.fromPage(dtoPage);

    }

    @Transactional(readOnly = true)
    public PaginationResponse<ProductDetailResponseDTO> filterProducts(
            Integer brandId, Integer categoryId, Integer colorId, Integer sizeId,
            Integer minPrice, Integer maxPrice, Pageable pageable){
        Specification<ProductDetail> spec = null;

        if (brandId != null) {
            spec = brandHasId(brandId);
        }
        if (categoryId != null) {
            spec = (spec == null) ? categoryHasId(categoryId) : spec.and(categoryHasId(categoryId));
        }
        if (colorId != null) {
            spec = (spec == null) ? colorHasId(colorId) : spec.and(colorHasId(colorId));
        }
        if (sizeId != null) {
            spec = (spec == null) ? sizeHasId(sizeId) : spec.and(sizeHasId(sizeId));
        }
        if (minPrice != null) {
            spec = (spec == null) ? priceGreaterThanOrEqual(minPrice) : spec.and(priceGreaterThanOrEqual(minPrice));
        }
        if (maxPrice != null) {
            spec = (spec == null) ? priceLessThanOrEqual(maxPrice) : spec.and(priceLessThanOrEqual(maxPrice));
        }

        // 3. Gọi repository.
        // Nhờ @EntityGraph, query này sẽ tự động JOIN FETCH
        Page<ProductDetail> productDetailPage = productDetailRepository.findAll(spec, pageable);

        // 4. Chuyển đổi sang DTO
        Page<ProductDetailResponseDTO> dtoPage = productDetailPage
                .map(productDetailMapper::toProductDetailDTO);

        return PaginationResponse.fromPage(dtoPage);
    }

    public ProductDetailResponseDTO getProductDetailById(Integer id) {
        ProductDetail pd = productDetailRepository.findById(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));
        return productDetailMapper.toProductDetailDTO(pd);
    }


    public ProductDetailResponseDTO createProductDetail(ProductDetailRequest request) {
        ProductDetail pd = new ProductDetail();
        return saveOrUpdateProductDetail(pd, request);
    }


    @Transactional
    public ProductDetailResponseDTO updateProductDetail(Integer id, ProductDetailRequest request) {
        ProductDetail pd = productDetailRepository.findById(id)
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        return saveOrUpdateProductDetail(pd, request);
    }


    @Transactional
    public void deleteProductDetail(Integer id) {
        if (!productDetailRepository.existsById(id)) {
            throw new WebErrorConfig(ErrorCode.PRODUCT_DETAIL_NOT_FOUND);
        }
        // Cần xử lý cẩn thận: Nếu đã có trong Order, có thể không cho xóa
        // hoặc chỉ đánh dấu là ngưng bán (soft delete).
        // Ở đây tôi dùng xóa cứng (hard delete) cơ bản:
        try {
            productDetailRepository.deleteById(id);
        } catch (Exception e) {
            throw new WebErrorConfig(ErrorCode.PRODUCT_DETAIL_IN_USE);
        }
    }

    private ProductDetailResponseDTO saveOrUpdateProductDetail(ProductDetail pd, ProductDetailRequest request) {
        pd.setDescription(request.getDescription());
        pd.setPrice(request.getPrice());
        pd.setQuantity(request.getQuantity());

        pd.setProduct(productRepository.findById(request.getProductId())
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.PRODUCT_NOT_FOUND)));

        pd.setColor(colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.COLOR_NOT_FOUND)));

        pd.setSize(sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.SIZE_NOT_FOUND)));

        pd.setImage(imageRepository.findById(request.getImageId())
                .orElseThrow(() -> new WebErrorConfig(ErrorCode.IMAGE_NOT_FOUND)));

        ProductDetail savedPd = productDetailRepository.save(pd);
        return productDetailMapper.toProductDetailDTO(savedPd);
    }

    // Lọc theo Brand (Join qua Product)
    private Specification<ProductDetail> brandHasId(Integer brandId) {
        return (root, query, cb) -> {
            Join<ProductDetail, Product> productJoin = root.join("product");
            Join<Product, Brand> brandJoin = productJoin.join("brand");
            return cb.equal(brandJoin.get("id"), brandId);
        };
    }

    // Lọc theo Category (Join qua Product)
    private Specification<ProductDetail> categoryHasId(Integer categoryId) {
        return (root, query, cb) -> {
            Join<ProductDetail, Product> productJoin = root.join("product");
            Join<Product, Category> categoryJoin = productJoin.join("category");
            return cb.equal(categoryJoin.get("id"), categoryId);
        };
    }

    // Lọc theo Color (Join trực tiếp)
    private Specification<ProductDetail> colorHasId(Integer colorId) {
        return (root, query, cb) -> cb.equal(root.join("color").get("id"), colorId);
    }

    // Lọc theo Size (Join trực tiếp)
    private Specification<ProductDetail> sizeHasId(Integer sizeId) {
        return (root, query, cb) -> cb.equal(root.join("size").get("id"), sizeId);
    }

    // Lọc theo giá
    private Specification<ProductDetail> priceGreaterThanOrEqual(Integer minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    private Specification<ProductDetail> priceLessThanOrEqual(Integer maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

}
