package com.qingcloud.saas.model.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页请求
 *
 * @author Alex
 */
@Data
public class BasePageRequest {
    @Schema(title = "当前第几页", defaultValue = "0")
    private Integer current;
    @Schema(title = "每页显示几个", defaultValue = "15")
    private Integer pageSize;

    @Schema(title = "查询关键词(可以不传)", required = false, nullable = true)
    private String searchWord;

    public <T> Page<T> buildPage() {
        if (current == null) {
            this.current = 0;
        }
        if (pageSize == null) {
            this.pageSize = 15;
        }
        return new Page<T>(this.current, this.pageSize);
    }


}
