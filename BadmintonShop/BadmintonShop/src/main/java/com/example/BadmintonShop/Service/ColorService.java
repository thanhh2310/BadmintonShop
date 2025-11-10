package com.example.BadmintonShop.Service;

import com.example.BadmintonShop.Config.WebErrorConfig;
import com.example.BadmintonShop.DTO.Request.ColorRequest;
import com.example.BadmintonShop.DTO.Response.ColorResponse;
import com.example.BadmintonShop.Enum.ErrorCode;
import com.example.BadmintonShop.Mapper.ColorMapper;
import com.example.BadmintonShop.Model.Color;
import com.example.BadmintonShop.Repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    public List<ColorResponse> getAllColors(){
        List<Color> colors = colorRepository.findAll();
        return colors.stream()
                .map(colorMapper::toColorResponse)
                .collect(Collectors.toList());
    }

    public ColorResponse getColorById(Integer id){
        Color color = colorRepository.findById(id)
                .orElseThrow(()->new WebErrorConfig( ErrorCode.COLOR_NOT_FOUND));
        return colorMapper.toColorResponse(color);
    }

    public ColorResponse createColor(ColorRequest request){
        if(colorRepository.findByName(request.getName()).isPresent()){
            throw new WebErrorConfig(ErrorCode.COLOR_ALREADY_EXISTS);
        }
        Color color = colorMapper.toColor(request);
        colorRepository.save(color);

        return colorMapper.toColorResponse(color);
    }

    @Transactional
    public ColorResponse updateColor(Integer id, ColorRequest request){
        Color color = colorRepository.findById(id)
                .orElseThrow(()-> new WebErrorConfig(ErrorCode.COLOR_NOT_FOUND));
        Optional<Color> colorWithNewName = colorRepository.findByName(request.getName());
        if (colorWithNewName.isPresent() && !colorWithNewName.get().getId().equals(id)) {
            throw new WebErrorConfig(ErrorCode.COLOR_ALREADY_EXISTS);
        }

        color.setName(request.getName());
        colorRepository.save(color);

        return colorMapper.toColorResponse(color);
    }

    @Transactional
    public void deleteColor(Integer id){
        Color existColor = colorRepository.findById(id)
                .orElseThrow(()->new WebErrorConfig( ErrorCode.COLOR_NOT_FOUND));
        try {
            colorRepository.delete(existColor);
        } catch (DataIntegrityViolationException e) {
            // Lỗi khóa ngoại nếu Color này đang được ProductDetail sử dụng
            throw new WebErrorConfig(ErrorCode.COLOR_IN_USE);
        }
    }
}
