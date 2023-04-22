package com.chadev.xcape.core.service.notification;

public interface NotificationInterface<NotificationRequest, NotificationResponse> {
    NotificationResponse sendMessage(NotificationRequest request);

}
