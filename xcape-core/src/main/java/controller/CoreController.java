package controller;

import controller.response.ErrorCode;
import controller.response.MerchantResponse;
import controller.response.Response;
import controller.response.ThemeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import service.MerchantService;
import service.ThemeService;

import java.util.List;

/**
 * 공통 API
 * GET "/merchants/{merchantId}" -> 한 지점 정보 가져오기
 * GET "/themes/{themeId}" -> 한 테마 정보 가져오기
 */

@RequiredArgsConstructor
@RestController
public class CoreController {

    private final MerchantService merchantService;
    private final ThemeService themeService;

    @GetMapping("/merchants/{merchantId}")
    public Response<MerchantResponse> getMerchant(@PathVariable Long merchantId) {
        MerchantResponse response = MerchantResponse.fromDto(merchantService.getMerchant(merchantId));
        return Response.success(response);
    }

    @GetMapping("/themes/{themeId}")
    public Response<ThemeResponse> getThemeByThemeId(@PathVariable Long themeId) {
        return Response.success(ThemeResponse.fromDto(themeService.getThemeByThemeId(themeId)));
    }

    //  테스트용
    @GetMapping("/all-merchants")
    public Response<List<MerchantResponse>> getAllMerchants() {
        try {
            List<MerchantResponse> merchantResponseList = merchantService.getAllMerchants().stream().map(MerchantResponse::fromDto).toList();
            return Response.success(merchantResponseList);
        } catch (Exception e) {
            return Response.error(ErrorCode.INVALID_PERMISSION);
        }
    }

}
