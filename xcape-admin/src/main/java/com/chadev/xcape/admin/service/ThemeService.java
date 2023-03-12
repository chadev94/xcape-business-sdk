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
import com.chadev.xcape.core.service.CorePriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThemeService {

    private final MerchantRepository merchantRepository;
    private final ThemeRepository themeRepository;
    private final CorePriceRepository priceRepository;
    private final CorePriceService corePriceService;
    private final CoreAbilityService coreAbilityService;
    private final S3Uploader s3Uploader;

    @Transactional
    public void createThemeByMerchantId(Long merchantId, ThemeModifyRequestDto requestDto, MultipartHttpServletRequest request, List<PriceDto> priceDtoList) throws IOException {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(IllegalArgumentException::new);
        imageUpload(requestDto, request);
        Theme newTheme = Theme.builder()
                .merchant(merchant)
                .activity(requestDto.getActivity())
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
                .observation(requestDto.getObservation())
                .point(requestDto.getPoint())
                .reasoning(requestDto.getReasoning())
                .teamwork(requestDto.getTeamwork())
                .youtubeLink(requestDto.getYoutubeLink())
                .build();
        Theme savedTheme = themeRepository.save(newTheme);
        for (PriceDto priceDto : priceDtoList) {
            priceRepository.save(new Price(priceDto, savedTheme));
        }
    }

    @Transactional
    public void modifyThemeDetail(Long themeId, ThemeModifyRequestDto requestDto, MultipartHttpServletRequest request) throws IOException {
        Theme updateTheme = themeRepository.findById(themeId).orElseThrow(IllegalArgumentException::new);
        imageUpload(requestDto, request);
        updateTheme.setNameKo(requestDto.getNameKo());
        updateTheme.setNameEn(requestDto.getNameEn());
        updateTheme.setMainImagePath(requestDto.getMainImagePath());
        updateTheme.setBgImagePath(requestDto.getBgImagePath());
        updateTheme.setTimetable(requestDto.getTimetable());
        updateTheme.setDescription(requestDto.getDescription());
        updateTheme.setReasoning(requestDto.getReasoning());
        updateTheme.setObservation(requestDto.getObservation());
        updateTheme.setActivity(requestDto.getActivity());
        updateTheme.setTeamwork(requestDto.getTeamwork());
        updateTheme.setMinParticipantCount(requestDto.getMinParticipantCount());
        updateTheme.setMaxParticipantCount(requestDto.getMaxParticipantCount());
        updateTheme.setDifficulty(requestDto.getDifficulty());
        updateTheme.setGenre(requestDto.getGenre());
        updateTheme.setPoint(requestDto.getPoint());
        updateTheme.setYoutubeLink(requestDto.getYoutubeLink());
        updateTheme.setColorCode(requestDto.getColorCode());
        updateTheme.setHasXKit(requestDto.getHasXKit());
        updateTheme.setIsCrimeScene(requestDto.getIsCrimeScene());
        corePriceService.savePriceList(requestDto.getPriceList(), updateTheme);
        coreAbilityService.saveAbilityList(requestDto.getAbilityList(), updateTheme);
    }

    public void imageUpload(ThemeModifyRequestDto requestDto, MultipartHttpServletRequest request) throws IOException {
        MultipartFile mainImage = request.getFile("mainImage");
        MultipartFile bgImage = request.getFile("bgImage");
        if (mainImage != null) {
            requestDto.setMainImagePath(s3Uploader.upload(mainImage, Long.toString(requestDto.getId())));
        }
        if (bgImage != null) {
            requestDto.setBgImagePath(s3Uploader.upload(bgImage, Long.toString(requestDto.getId())));
        }
    }

    public Theme test() {
        Theme themeWithPriceAndAbilityByThemeId = themeRepository.findThemeWithPriceAndAbilityByThemeId(1L);
        return themeWithPriceAndAbilityByThemeId;
    }
}
