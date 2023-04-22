package com.chadev.xcape.api.util.notification.kakao;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class KakaoTalkResponse {

    public Header header;
    public Message message;

    @Getter
    @Setter
    public static class Header {
        public int resultCode;
        public String resultMessage;
        public boolean isSuccessful;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Message {
        public String requestId;
        public String senderGroupingKey;
        public List<SendResult> sendResults;

    }

    @Getter
    @Setter
    @Builder
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

