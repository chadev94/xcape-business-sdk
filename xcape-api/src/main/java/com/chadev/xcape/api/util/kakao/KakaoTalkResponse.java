package com.chadev.xcape.api.util.kakao;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KakaoTalkResponse {

    public Header header;
    public Message message;

    public static class Header {
        public String resultCode;
        public String resultMessage;
        public String isSuccessful;

    }

    public static class Message {
        public String requestId;
        public String senderGroupingKey;
        public List<SendResult> sendResults;

    }

    public static class SendResult {
        public String recipientSeq;
        public String recipientNo;
        public String resultCode;
        public String resultMessage;
        public String recipientGroupingKey;
    }

}

