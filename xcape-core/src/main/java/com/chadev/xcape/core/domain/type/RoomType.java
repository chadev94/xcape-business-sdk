package com.chadev.xcape.core.domain.type;

import lombok.Getter;

@Getter
public enum RoomType {
    OPEN_ROOM, GENERAL;

    public boolean is(RoomType roomType) {
        return this == roomType;
    }
}
