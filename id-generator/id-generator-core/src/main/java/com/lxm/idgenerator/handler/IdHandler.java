package com.lxm.idgenerator.handler;

import com.lxm.idgenerator.bean.IdMeta;
import com.lxm.idgenerator.service.bean.Id;

public interface IdHandler {
    /**
     * 对id对象赋值其序列号和时间戳字段
     * @param timer
     * @param id
     * @param meta
     * @return 返回最后需要的long型id值
     */
    long handle(TimeHandler timer, Id id, IdMeta meta);

}
