package com.qingcloud.saas.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alex
 */
@Slf4j
public class PageBeanUtils {
    /**
     * 转换分页类
     *
     * @param page   旧的分页对象
     * @param tClass 要转换的类型
     * @param <T>
     * @return
     */
    public static <A, T extends A> Page<T> copyPage(IPage<A> page, Class<T> tClass) {
        List<T> copy = copy(page.getRecords(), tClass);
        Page<T> newPage = new Page<>();
        newPage.setCurrent(page.getCurrent());
        newPage.setSize(page.getSize());
        newPage.setTotal(page.getTotal());
        newPage.setPages(page.getPages());
        newPage.setRecords(copy);
        return newPage;
    }

    public static <T> List<T> copy(List<?> input, Class<T> tClass) {
        return input.stream().map(o -> {
            T t = null;
            try {
                t = tClass.newInstance();
            } catch (Exception e) {
                log.error("bean copy异常", e);
            }
            org.springframework.beans.BeanUtils.copyProperties(o, t);
            return t;
        }).collect(Collectors.toList());
    }

}
