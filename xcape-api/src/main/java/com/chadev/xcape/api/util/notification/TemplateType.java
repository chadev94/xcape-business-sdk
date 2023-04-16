package com.chadev.xcape.api.util.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TemplateType {

    AUTHENTICATION("template-authentication"),
    REGISTER_RESERVATION("template-reservation-register"),
    ;

    private final String templateId;
}
