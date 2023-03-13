package com.chadev.xcape.core.domain.type;

public enum UseType {
    Y(true),
    N(false);
    
    final boolean value;

    UseType(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }
}
