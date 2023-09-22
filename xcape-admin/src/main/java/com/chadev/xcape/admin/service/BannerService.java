package com.chadev.xcape.admin.service;

import com.chadev.xcape.admin.util.S3Uploader;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.BannerDto;
import com.chadev.xcape.core.domain.entity.Banner;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.repository.BannerRepository;
import com.chadev.xcape.core.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final MerchantRepository merchantRepository;
    private final DtoConverter dtoConverter;
    private final S3Uploader s3Uploader;

    public List<BannerDto> getBannerListByMerchantId(Long merchantId) {
        return bannerRepository.findBannersByMerchantId(merchantId).stream().map(dtoConverter::toBannerDto).toList();
    }

    @Transactional
    public void modifyBannerListByMerchantId(Long merchantId, List<BannerDto> bannerDtoList) {
        List<Banner> bannerList = bannerRepository.findBannersByMerchantId(merchantId);

        bannerDtoList.forEach(bannerDto -> {
            Banner updateBanner = bannerList.stream()
                    .filter(banner -> Objects.equals(banner.getId(), bannerDto.getId()))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            updateBanner.setType(bannerDto.getType());
            updateBanner.setSequence(bannerDto.getSequence());
            updateBanner.setUseYn(bannerDto.getUseYn());
        });
    }

    @Transactional
    public void createBannerByMerchantId(Long merchantId, BannerDto bannerDto, MultipartHttpServletRequest request) throws IOException {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        bannerImageUpload(bannerDto, request);
        Banner newBanner = Banner.builder()
                .imagePath(bannerDto.getImagePath())
                .link(bannerDto.getLink())
                .description(bannerDto.getDescription())
                .merchant(merchant)
                .build();
        bannerRepository.save(newBanner);
    }

    public void bannerImageUpload(BannerDto bannerDto, MultipartHttpServletRequest request) throws IOException {
        MultipartFile bannerImage = request.getFile("bannerImage");
        if (bannerImage != null && !bannerImage.isEmpty()) {
            bannerDto.setImagePath(s3Uploader.upload(bannerImage, "banner/" + Long.toString(bannerDto.getMerchantId())));
        }
    }

    public BannerDto getBannerDetail(Long bannerId) {
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(IllegalArgumentException::new);
        return dtoConverter.toBannerDto(banner);
    }

    @Transactional
    public void modifyBannerDetail(Long bannerId, BannerDto bannerDto, MultipartHttpServletRequest request) throws IOException {
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(IllegalArgumentException::new);
        bannerDto.setMerchantId(banner.getMerchant().getId());
        bannerImageUpload(bannerDto, request);
        if (bannerDto.getImagePath() != null) {
            banner.setImagePath(bannerDto.getImagePath());
        }
        banner.setDescription(bannerDto.getDescription());
        banner.setLink(bannerDto.getLink());
    }
}
