package com.qingcloud.saas.model.common;

import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Alex
 */
@Schema(hidden = true, subTypes = {QingResp.class, BasePageRequest.class, RetData.class,RetDataPage.class})
@Data
public class Ret {
    /**
     * 返回🐴
     */
    @Schema(description = "返回🐴")
    private Integer code;

    /**
     * 错误消息
     */
    @Schema(description = "错误消息")
    private String message;

    public static Ret ok() {
        Ret ret = new Ret();
        ret.setCode(200);
        return ret;
    }

    public static <T> RetData<T> ok(T data) {
        RetData<T> retData = new RetData<>();
        retData.setCode(200);
        retData.setData(data);
        return retData;
    }

    /**
     * 返回分页
     */
    public static <T> Ret page(IPage<T> page) {
        RetDataPage<List<T>> ret = new RetDataPage<List<T>>();
        ret.setCode(200);
        ret.setData(page.getRecords());
        ret.setPage(page.getCurrent());
        ret.setSize(page.getSize());
        ret.setTotal(page.getTotal());
        ret.setTotalPage(page.getPages());
        return ret;
    }

    public static Ret err() {
        return err(null);
    }

    public static Ret err(String msg) {
        Ret ret = new Ret();
        ret.setCode(400);
        ret.setMessage(msg);
        return ret;
    }
}
