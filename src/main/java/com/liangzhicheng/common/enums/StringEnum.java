package com.liangzhicheng.common.enums;

import lombok.Getter;

/**
 * 字符串枚举类
 * @author liangzhicheng
 */
@Getter
public enum StringEnum {

    DEL_USER("del_user"),
    DEL_FLAG("del_flag"),

    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4");

    private String value;

    StringEnum(String value){
        this.value = value;
    }

}
