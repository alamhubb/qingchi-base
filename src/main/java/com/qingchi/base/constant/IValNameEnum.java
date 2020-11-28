package com.qingchi.base.constant;

public interface IValNameEnum<T extends Enum<T>> {
    String getValue();

    String getName();
}