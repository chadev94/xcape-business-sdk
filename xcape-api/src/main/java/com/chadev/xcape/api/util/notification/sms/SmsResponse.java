package com.chadev.xcape.api.util.notification.sms;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SmsResponse {

    private Header header;
    private Body body;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Body{
        public Data data;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Data{
        public String requestId;
        public String statusCode;
        public ArrayList<SendResultList> sendResultList;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Header{
        public int resultCode;
        public String resultMessage;
        public boolean isSuccessful;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class SendResultList{
        public String recipientNo;
        public Integer resultCode;
        public String resultMessage;
        public int recipientSeq;
    }
}
