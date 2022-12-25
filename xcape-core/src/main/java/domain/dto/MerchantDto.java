package domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MerchantDto {
    private Long id;
    private Long accountId;
    private String name;
    private String address;

    private List<ThemeDto> themeDtoList;

    public MerchantDto(Long id, Long accountId, String name, String address) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
        this.address = address;
    }
}