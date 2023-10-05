package com.chadev.xcape.core.service.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Header {
    public int resultCode;
    public String resultMessage;
    public boolean isSuccessful;
}
