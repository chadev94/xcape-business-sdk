package com.chadev.xcape.api.controller.request;

import com.chadev.xcape.core.service.notification.NotificationTemplateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthenticationRequest {

    private String reservationId;
    private String recipientNo;
    private boolean canceled;

    public NotificationTemplateEnum.AuthenticationParam getAuthenticationParam(String authenticationNumber) {
        return new NotificationTemplateEnum.AuthenticationParam(reservationId, recipientNo, authenticationNumber);
    }
}
