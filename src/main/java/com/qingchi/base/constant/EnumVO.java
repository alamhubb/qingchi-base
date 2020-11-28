package com.qingchi.base.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinkaiyuan
 * @date 2018-09-16 11:04
 */
public class EnumVO {
    /**
     * 枚举值
     */
    private String value;
    /**
     * 枚举中文名
     */
    private String name;

    public EnumVO(IValNameEnum iEnum) {
        this.value = iEnum.getValue();
        this.name = iEnum.getName();
    }

    /**
     * 将枚举转为list，传入枚举数组，返回EnumVOList
     *
     * @param enums
     * @return
     */
    public static List<EnumVO> getEnumVOList(IValNameEnum[] enums) {
        List<EnumVO> enumVOS = new ArrayList<>();
        // 遍历游戏种类枚举放入list
        for (IValNameEnum iEnum : enums) {
            enumVOS.add(new EnumVO(iEnum));
        }
        return enumVOS;
    }
}
