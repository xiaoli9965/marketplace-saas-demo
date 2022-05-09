package com.qingcloud.saas.model.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Alex
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RetDataPage<T> extends RetData<T> {
    /**
     * 总数
     */
    private long total;
    /**
     * 页码
     */
    private long page;
    /**
     * 每页结果数
     */
    private long size;
    /**
     * 总页数
     */
    private long totalPage;

}
