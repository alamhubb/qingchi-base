package com.qingchi.base.constant;

/**
 * @author qinkaiyuan
 * @date 2019-09-28 10:06
 */
public enum GenderTypeEnum {
    male(1, "男"),
    female(2, "女");
    /**
     * 枚举值
     */
    private Integer value;
    /**
     * 枚举中文名
     */
    private String name;

    GenderTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    /**
     * 从int到enum的转换函数
     *
     * @param value 整数值
     * @return 枚举值，如果没有对应的枚举返回为null
     */
    public static GenderTypeEnum enumOf(Integer value) {
        for (GenderTypeEnum iEnum : GenderTypeEnum.values()) {
            if (value.equals(iEnum.value)) {
                return iEnum;
            }
        }
        return null;
    }

    public static GenderTypeEnum nameOf(String name) {
        for (GenderTypeEnum iEnum : GenderTypeEnum.values()) {
            if (iEnum.name.equals(name)) {
                return iEnum;
            }
        }
        return null;
    }
}
