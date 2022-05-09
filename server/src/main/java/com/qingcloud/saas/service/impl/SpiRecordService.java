package com.qingcloud.saas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingcloud.saas.mapper.ISpiRecordMapper;
import com.qingcloud.saas.model.TbSpiRecord;
import com.qingcloud.saas.service.ISpiRecordService;
import org.springframework.stereotype.Service;

/**
 * @author Alex
 */
@Service
public class SpiRecordService  extends ServiceImpl<ISpiRecordMapper, TbSpiRecord> implements ISpiRecordService {
}
