package com.lxm.idgenerator.service.intf;

import com.lxm.idgenerator.service.bean.Id;

import java.util.Date;

public interface IdService {

    /**
     * 生成id
     * @return
     */
    long genId();

    /**
     * 批量生成id
     * @param count 要生成的数量
     * @return
     */
    long[] batchGenId(int count);

    /**
     * 将id解码成有含义的Id对象
     * @param id
     * @return
     */
    Id decode(long id);

    /**
     * 根据需求,手动生成id
     * @param time
     * @param seq
     * @param workerId
     * @param dataCenterId
     * @return
     */
    long encode(long time, long dataCenterId, long workerId, long seq);

    /**
     * 启用zookeeper注册workerId时有效,用于刷新workerId
     */
    void refreshWorkerId();

    /**
     * 解析时间戳
     * @param time
     * @return
     */
    Date transTime(long time);

}
