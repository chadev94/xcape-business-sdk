package com.chadev.xcape.api.service;

import com.chadev.xcape.api.repository.BannerRepository;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.BannerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final DtoConverter dtoConverter;

    public List<BannerDto> getAllBannerList() {
        return bannerRepository.findAll().stream().map(dtoConverter::toBannerDto).toList();
    }
}
