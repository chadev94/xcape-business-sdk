package com.chadev.xcape.core.domain.type;

import lombok.Getter;

@Getter
public enum HistoryType {
    REGISTER,
    MODIFY,
    CANCEL,
    ;

    public boolean is(HistoryType historyType) {
        return this == historyType;
    }
}
