package service;

import domain.dto.MerchantDto;
import domain.dto.ThemeDto;
import domain.entity.Admin;
import domain.entity.Merchant;
import domain.entity.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AdminRepository;
import repository.MerchantRepository;
import repository.ThemeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CoreService {

    private final MerchantRepository merchantRepository;
    private final ThemeRepository themeRepository;
    private final AdminRepository adminRepository;

    //  MerchantService
    @Transactional
    public List<MerchantDto> getMerchantsByAdmin(Admin admin) {
        List<Merchant> merchantByUserId = merchantRepository.findMerchantsByAdmin(admin);

        return merchantByUserId.stream()
                .map(this::merchantToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MerchantDto getMerchant(Long merchantId) {
        Optional<Merchant> optional = merchantRepository.findById(merchantId);
        assert optional.isPresent();

        return merchantToDto(optional.get());
    }

    //  테스트용
    @Transactional
    public List<MerchantDto> getAllMerchants() {
        List<Merchant> merchantByUserId = merchantRepository.findAll();

        return merchantByUserId.stream()
                .map(this::merchantToDto)
                .collect(Collectors.toList());
    }
    //  MerchantService ---end

    //  ThemeService

    @Transactional
    public List<ThemeDto> getThemesByMerchantId(Admin admin, Long merchantId) {
        //  검증
        Optional<Merchant> optional = merchantRepository.findById(merchantId);
        assert optional.isPresent();

        Merchant merchant = optional.get();
        return themeRepository.findThemesByMerchant(merchant).stream().map(ThemeDto::fromEntity).toList();
    }

    @Transactional
    public ThemeDto getThemeByThemeId(Long themeId) {
        //  검증
        Optional<Theme> optional = themeRepository.findById(themeId);
        assert optional.isPresent();

        return ThemeDto.fromEntity(optional.get());
    }

    //  Util
    //  merchantId 에 해당하는 merchant 가 없을 시 AssertionError 발생
    private Theme toEntity(ThemeDto dto) {
        assert merchantRepository.findById(dto.getMerchantId()).isPresent();
        return Theme.of(
                merchantRepository.findById(dto.getMerchantId()).get(),
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

    private MerchantDto merchantToDto(Merchant merchant) {
        List<ThemeDto> collect = themeRepository.findThemesByMerchant(merchant).stream().map(ThemeDto::fromEntity).toList();
        return new MerchantDto(merchant.getId(), merchant.getAdmin().getId(), merchant.getName(), merchant.getAddress(), collect);
    }

}
