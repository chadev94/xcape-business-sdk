package com.chadev.xcape.admin.service;

import com.chadev.xcape.admin.repository.MerchantRepository;
import com.chadev.xcape.admin.repository.ThemeRepository;
import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final MerchantRepository merchantRepository;
    private final ThemeRepository themeRepository;
    private final DtoConverter dtoConverter;

    public ThemeDto getTheme(Long themeId) {
        Optional<Theme> theme = themeRepository.findById(themeId);
        return theme.map(dtoConverter::toThemeDto).orElse(null);
    }

    public void modifyThemeDetail(Long themeId, ThemeDto themeDto) {
        Optional<Merchant> merchant = merchantRepository.findById(themeDto.getMerchantId());
        if (merchant.isPresent()) {
            Optional<Theme> theme = themeRepository.findById(themeId);
            if (theme.isPresent()) {
                theme.get().setMerchant(merchant.get());
                theme.get().setName(themeDto.getName());
                themeRepository.save(theme.get());
            }
        }
    }

    public void createThemeByMerchantId(Long merchantId, ThemeDto themeDto) {
        Optional<Merchant> merchant = merchantRepository.findById(merchantId);
        if (merchant.isPresent()) {
            Theme newTheme = Theme.builder()
                    .merchant(merchant.get())
                    .activity(themeDto.getActivity())
                    .bgImage(themeDto.getBgImage())
                    .colorCode(themeDto.getColorCode())
                    .description(themeDto.getDescription())
                    .difficulty(themeDto.getDifficulty())
                    .genre(themeDto.getGenre())
                    .hasXKit(themeDto.getHasXKit())
                    .isCrimeScene(themeDto.getIsCrimeScene())
                    .mainImage(themeDto.getMainImage())
                    .maxPersonnel(themeDto.getMaxPersonnel())
                    .minPersonnel(themeDto.getMinPersonnel())
                    .name(themeDto.getName())
                    .observation(themeDto.getObservation())
                    .point(themeDto.getPoint())
                    .price(themeDto.getPrice())
                    .reasoning(themeDto.getReasoning())
                    .teamwork(themeDto.getTeamwork())
                    .youtubeLink(themeDto.getYoutubeLink())
                    .build();

            themeRepository.save(newTheme);
        }
    }
}
