package com.qingchi.base.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author qinkaiyuan
 * @date 2018-09-16 18:26
 */
public class PageableUtils {
    public static Pageable getTalkPageable() {
        return PageRequest.of(0, 100, Sort.Direction.DESC, "date");
    }

    public static Pageable getTalkPageable(Integer size) {
        return PageRequest.of(0, size, Sort.Direction.DESC, "createTime");
    }
}
