package com.chadev.xcape.core.service.notification.kakao;

import com.chadev.xcape.core.service.notification.Header;
import com.chadev.xcape.core.service.notification.NotificationResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class KakaoTalkResponse implements NotificationResponse {
    public Header header;
    public Message message;

    @Getter
    @Setter
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Message {
        public String requestId;
        public String senderGroupingKey;
        public List<SendResult> sendResults;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class SendResult {
        public int recipientSeq;
        public String recipientNo;
        public int resultCode;
        public String resultMessage;
        public String recipientGroupingKey;
    }

}

