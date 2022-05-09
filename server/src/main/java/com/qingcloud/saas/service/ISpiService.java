package com.qingcloud.saas.service;

import com.qingcloud.saas.model.common.QingResp;
import com.qingcloud.saas.model.common.qing.QingRequest;
import org.springframework.http.ResponseEntity;

public interface ISpiService {

    /**
     * 创建实例
     */
    ResponseEntity<QingResp> create(QingRequest req);

    /**
     * 续费实例
     */
    ResponseEntity<QingResp> renew(QingRequest req);

    /**
     * 升级实例
     */
    ResponseEntity<QingResp> upgrade(QingRequest req);

    ResponseEntity<QingResp> expire(QingRequest req);

    ResponseEntity<QingResp> delete(QingRequest req);
}
