package com.chadev.xcape.core.service;

import com.chadev.xcape.core.domain.converter.DtoConverter;
import com.chadev.xcape.core.domain.dto.ThemeDto;
import com.chadev.xcape.core.domain.entity.Account;
import com.chadev.xcape.core.domain.entity.Merchant;
import com.chadev.xcape.core.domain.entity.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chadev.xcape.core.repository.CoreMerchantRepository;
import com.chadev.xcape.core.repository.CoreThemeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoreThemeService {

    private final CoreThemeRepository coreThemeRepository;
    private final CoreMerchantRepository coreMerchantRepository;
    private final DtoConverter dtoConverter;


    @Transactional(readOnly = true)
    public List<ThemeDto> getThemesByMerchantId(Account account, Long merchantId) {
        Optional<Merchant> findById = coreMerchantRepository.findById(merchantId);
        if (findById.isPresent()) {
            List<Theme> themesByMerchant = coreThemeRepository.findThemesByMerchant(findById.get());
            return themesByMerchant.stream().map(dtoConverter::toThemeDto).collect(Collectors.toList());
        }
        return null;
    }

    @Transactional
    public void createThemeByMerchantId(Long merchantId, ThemeDto dto) {
        dto.setMerchantId(merchantId);
        coreThemeRepository.save(toEntity(dto));
    }

    @Transactional(readOnly = true)
    public ThemeDto getThemeByThemeId(Long themeId) {
        Optional<Theme> findById = coreThemeRepository.findById(themeId);
        return findById.map(dtoConverter::toThemeDto).orElse(null);
    }

    @Transactional
    public void modifyThemeById(Long themeId, ThemeDto dto) {
        assert coreThemeRepository.findById(themeId).isPresent();

//        themeRepository.modifyThemeById(themeId, dto.getName(), dto.getMainImage(), dto.getBgImage(), dto.getPrice(), dto.getDescription(), dto.getReasoning(), dto.getObservation(), dto.getActivity(), dto.getTeamwork(), dto.getMinPersonnel(), dto.getMaxPersonnel(), dto.getDifficulty(), dto.getGenre(), dto.getPoint(), dto.getYoutubeLink(), dto.getColorCode(), dto.getHasXKit(), dto.getIsCrimeScene());
    }

    //  merchantId 에 해당하는 merchant 가 없을 시 AssertionError 발생
    private Theme toEntity(ThemeDto dto) {
        assert coreMerchantRepository.findById(dto.getMerchantId()).isPresent();
        return Theme.of(
                coreMerchantRepository.findById(dto.getMerchantId()).get(),
                dto.getName(),
                dto.getMainImage(),
                dto.getBgImage(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getReasoning(),
                dto.getObservation(),
                dto.getActivity(),
                dto.getTeamwork(),
                dto.getMinPersonnel(),
                dto.getMaxPersonnel(),
                dto.getDifficulty(),
                dto.getGenre(),
                dto.getPoint(),
                dto.getYoutubeLink(),
                dto.getColorCode(),
                dto.getHasXKit(),
                dto.getIsCrimeScene()
        );
    }
}
