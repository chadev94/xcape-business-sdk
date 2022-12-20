package controller;

import controller.response.ErrorCode;
import controller.response.MerchantResponse;
import controller.response.Response;
import domain.entity.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import service.CoreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CoreController {

    private final CoreService coreService;

    @GetMapping("/merchants/{merchantId}")
    public Response<MerchantResponse> getMerchant(Admin admin, @PathVariable Long merchantId) {
        MerchantResponse response = MerchantResponse.fromDto(coreService.getMerchant(merchantId));
        return Response.success(response);
    }

    @GetMapping("/merchants")
    public Response<List<MerchantResponse>> getMerchants(Admin admin) {
        try {
            List<MerchantResponse> merchantResponseList = coreService.getMerchantsByAdmin(admin).stream().map(MerchantResponse::fromDto).toList();
            return Response.success(merchantResponseList);
        } catch (Exception e) {
            return Response.error(ErrorCode.INVALID_PERMISSION);
        }
    }

    @GetMapping("/all-merchants")
    public Response<List<MerchantResponse>> getAllMerchants() {
        try {
            List<MerchantResponse> merchantResponseList = coreService.getAllMerchants().stream().map(MerchantResponse::fromDto).toList();
            return Response.success(merchantResponseList);
        } catch (Exception e) {
            return Response.error(ErrorCode.INVALID_PERMISSION);
        }
    }

}
