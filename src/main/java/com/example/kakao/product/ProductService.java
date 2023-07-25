package com.example.kakao.product;

import com.example.kakao._core.errors.exception.Exception404;
import com.example.kakao.option.Option;
import com.example.kakao.option.OptionJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductJPARepository productRepository;
    private final OptionJPARepository optionRepository;

    public ProductResponse.FindByIdDTO findById(int id) {
        Product productPS = productRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 상품을 찾을 수 없습니다 : "+id)
        );
        List<Option> optionListPS = optionRepository.findByProductId(productPS.getId());
        return new ProductResponse.FindByIdDTO(productPS, optionListPS);
    }

    public List<ProductResponse.FindAllDTO> findAll(int page) {
        Pageable pageable = PageRequest.of(page,9);
        Page<Product> pageContent = productRepository.findAll(pageable);
        List<ProductResponse.FindAllDTO> responseDTOs = pageContent.getContent().stream().map(ProductResponse.FindAllDTO::new).collect(Collectors.toList());
        return responseDTOs;
    }
}