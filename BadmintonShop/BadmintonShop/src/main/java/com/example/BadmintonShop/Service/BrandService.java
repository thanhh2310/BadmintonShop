package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.BrandRequest;
import com.example.BadmintonShop.DTO.Response.BrandResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.BrandMapper;
import com.example.BadmintonShop.Model.Brand;
import com.example.BadmintonShop.Repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public List<BrandResponse> getAllBrands(){
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(brandMapper::toBrandResponse)
                .collect(Collectors.toList());
    }

    public BrandResponse getBrandById(Integer id){
        Brand brand = brandRepository.findById(id)
                .orElseThrow(()->new WebErrorConfig( ErrorCode.BRAND_NOT_FOUND));
        return brandMapper.toBrandResponse(brand);
    }

    public BrandResponse createBrand(BrandRequest request){
        if(brandRepository.findByName(request.getName()).isPresent()){
            throw new WebErrorConfig(ErrorCode.BRAND_ALREADY_EXISTS);
        }
        Brand brand = brandMapper.toBrand(request);
        brandRepository.save(brand);

        return brandMapper.toBrandResponse(brand);
    }

    @Transactional
    public BrandResponse updateBrand(Integer id, BrandRequest request){
        Brand brand = brandRepository.findById(id)
                .orElseThrow(()-> new WebErrorConfig(ErrorCode.BRAND_NOT_FOUND));
        Optional<Brand> brandWithNewName = brandRepository.findByName(request.getName());
        if (brandWithNewName.isPresent() && !brandWithNewName.get().getId().equals(id)) {
            throw new WebErrorConfig(ErrorCode.BRAND_ALREADY_EXISTS);
        }

        brand.setName(request.getName());

        brandRepository.save(brand);

        return brandMapper.toBrandResponse(brand);
    }

    @Transactional
    public void deleteBrand(Integer id){
        Brand existBrand = brandRepository.findById(id)
                .orElseThrow(()->new WebErrorConfig( ErrorCode.BRAND_NOT_FOUND));
        try {
            brandRepository.delete(existBrand);
        } catch (DataIntegrityViolationException e) {
            throw new WebErrorConfig(ErrorCode.BRAND_IN_USE);
        }
    }

}
