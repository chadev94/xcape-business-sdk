package com.chadev.xcape.core.domain.entity.history;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum HistoryType {
    REGISTER("register"),
    MODIFY("modify"),
    CANCEL("cancel"),
    ;

    private String type;
}
