package com.chadev.xcape.admin.service;

import com.chadev.xcape.admin.repository.MerchantRepository;
import com.chadev.xcape.admin.repository.ThemeRepository;
import com.chadev.xcape.admin.util.S3Uploader;
import com.chadev.xcape.core.domain.dto.PriceDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Price;
import com.chadev.xcape.core.domain.entity.Theme;
import com.chadev.xcape.core.domain.request.ThemeModifyRequestDto;
import com.chadev.xcape.core.repository.CorePriceRepository;
import com.chadev.xcape.core.service.CoreAbilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@EnableCaching
@RequiredArgsConstructor
public class ThemeService {

    private final MerchantRepository merchantRepository;
    private final ThemeRepository themeRepository;
    private final CorePriceRepository priceRepository;
    private final CoreAbilityService coreAbilityService;
    private final S3Uploader s3Uploader;

    @Transactional
    public void createThemeByMerchantId(Long merchantId, ThemeModifyRequestDto requestDto, MultipartHttpServletRequest request, List<PriceDto> priceDtoList) throws IOException {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        themeImageUpload(requestDto, request);
        Theme newTheme = Theme.builder()
                .merchant(merchant)
                .bgImagePath(requestDto.getBgImagePath())
                .colorCode(requestDto.getColorCode())
                .description(requestDto.getDescription())
                .difficulty(requestDto.getDifficulty())
                .genre(requestDto.getGenre())
                .hasXKit(requestDto.getHasXKit())
                .isCrimeScene(requestDto.getIsCrimeScene())
                .mainImagePath(requestDto.getMainImagePath())
                .minParticipantCount(requestDto.getMinParticipantCount())
                .maxParticipantCount(requestDto.getMaxParticipantCount())
                .nameKo(requestDto.getNameKo())
                .nameEn(requestDto.getNameEn())
                .point(requestDto.getPoint())
                .youtubeLink(requestDto.getYoutubeLink())
                .build();
        Theme savedTheme = themeRepository.save(newTheme);
        for (PriceDto priceDto : priceDtoList) {
            priceRepository.save(new Price(priceDto, savedTheme.getMerchant(), savedTheme));
        }
    }

    @Transactional
    public void modifyThemeDetail(Long themeId, ThemeModifyRequestDto requestDto, MultipartHttpServletRequest request) throws IOException {
        Theme updateTheme = themeRepository.findById(themeId).orElseThrow(IllegalArgumentException::new);
        themeImageUpload(requestDto, request);
        if (requestDto.getMainImagePath() != null) {
            updateTheme.setMainImagePath(requestDto.getMainImagePath());
        }
        if (requestDto.getMainImagePath() != null) {
            updateTheme.setBgImagePath(requestDto.getBgImagePath());
        }
        updateTheme.setNameKo(requestDto.getNameKo());
        updateTheme.setNameEn(requestDto.getNameEn());
//        updateTheme.setTimetable(requestDto.getTimetable());
        updateTheme.setDescription(requestDto.getDescription());
        updateTheme.setMinParticipantCount(requestDto.getMinParticipantCount());
        updateTheme.setMaxParticipantCount(requestDto.getMaxParticipantCount());
        updateTheme.setDifficulty(requestDto.getDifficulty());
        updateTheme.setGenre(requestDto.getGenre());
        updateTheme.setPoint(requestDto.getPoint());
        updateTheme.setYoutubeLink(requestDto.getYoutubeLink());
        updateTheme.setColorCode(requestDto.getColorCode());
        updateTheme.setHasXKit(requestDto.getHasXKit());
        updateTheme.setIsCrimeScene(requestDto.getIsCrimeScene());
        coreAbilityService.saveAbilityList(requestDto.getAbilityList(), updateTheme);
    }

    public void themeImageUpload(ThemeModifyRequestDto requestDto, MultipartHttpServletRequest request) throws IOException {
        MultipartFile mainImage = request.getFile("mainImage");
        MultipartFile bgImage = request.getFile("bgImage");
        if (mainImage != null && !mainImage.isEmpty()) {
            requestDto.setMainImagePath(s3Uploader.upload(mainImage, Long.toString(requestDto.getThemeId())));
        }
        if (bgImage != null && !bgImage.isEmpty()) {
            requestDto.setBgImagePath(s3Uploader.upload(bgImage, Long.toString(requestDto.getThemeId())));
        }
    }
}
