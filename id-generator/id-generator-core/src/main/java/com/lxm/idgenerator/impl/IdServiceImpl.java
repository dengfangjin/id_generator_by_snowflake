package com.lxm.idgenerator.impl;

import com.lxm.idgenerator.bean.IdMeta;
import com.lxm.idgenerator.bean.MillisecondIdMeta;
import com.lxm.idgenerator.bean.SecondIdMeta;
import com.lxm.idgenerator.enums.IdType;
import com.lxm.idgenerator.handler.IdHandler;
import com.lxm.idgenerator.handler.LockIdHandler;
import com.lxm.idgenerator.handler.TimeHandler;
import com.lxm.idgenerator.provider.DatacenterIdProvider;
import com.lxm.idgenerator.provider.ZookeeperWorkerIdProvider;
import com.lxm.idgenerator.service.bean.Id;
import com.lxm.idgenerator.provider.WorkerIdProvider;
import com.lxm.idgenerator.service.intf.IdService;
import com.lxm.idgenerator.util.BitUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Id-Service实现类
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/6/27
 * @time 11:20
 */
public class IdServiceImpl implements IdService {
    long workerId = -1;
    long dataCenterId = -1;
    IdMeta meta;
    WorkerIdProvider workerIdProvider;
    DatacenterIdProvider datacenterIdProvider;
    TimeHandler timer;
    IdHandler idHandler;
    IdType idType;

    public IdServiceImpl(DatacenterIdProvider datacenterIdProvider, WorkerIdProvider workerIdProvider, IdType idType) {
        this.datacenterIdProvider = datacenterIdProvider;
        this.workerIdProvider = workerIdProvider;
        this.idType = idType;
        this.init();
    }

    private void init() {
        if (this.idType == null) {
            throw new RuntimeException("id类型未指定");
        }
        if (this.meta == null) {
            if (this.idType == IdType.SECOND) {
                this.meta = new SecondIdMeta();
            } else if (this.idType == IdType.MILLISECOND) {
                this.meta = new MillisecondIdMeta();
            } else {
                throw new RuntimeException("未能识别的IdMeta");
            }
        }

        if (this.timer == null) {
            this.timer = new TimeHandler(this.meta);
        }

        this.dataCenterId = datacenterIdProvider.getDatacenterId();
        validateDataCenterId();
        this.workerId = workerIdProvider.getWorkerId();
        validateWorkerId();

        if (idHandler == null) {
            idHandler = new LockIdHandler();
        }
    }

    public long[] batchGenId(int count) {
        validateWorkerId();
        long[] ret = new long[count];
        Id id = new Id();
        id.setWorkerId(workerId);
        id.setDatacenterId(dataCenterId);
        for (int i = 0; i < count; i++) {
            long tmpId = idHandler.handle(timer, id, meta);
            ret[i] = tmpId;
        }
        return ret;
    }
    
    public long genId() {
        validateWorkerId();
        Id id = new Id();
        id.setWorkerId(workerId);
        id.setDatacenterId(dataCenterId);
        long ret = idHandler.handle(timer, id, meta);
        return ret;
    }

    
    public Id decode(long id) {
        return BitUtil.long2Id(id, meta);
    }


    public long encode(long time, long dataCenterId, long workerId, long seq) {
        Id id = new Id(time, dataCenterId, workerId, seq);
        return BitUtil.id2long(id, this.meta);
    }

    public void refreshWorkerId() {
        this.workerId = -1;
        ZookeeperWorkerIdProvider provider = (ZookeeperWorkerIdProvider) this.workerIdProvider;
        provider.register();
        this.workerId = provider.getWorkerId();
    }

    public Date transTime(long time) {
        return timer.transTime(time);
    }

    private void validateWorkerId() {
        if (workerId < 0 || workerId > BitUtil.max(meta.getWorkerBits())) {
            throw new RuntimeException(String.format("WorkerId错误,当前workerId=%d, 合法范围0~%d", workerId,
                    BitUtil.max(meta.getWorkerBits())));
        }
    }

    private void validateDataCenterId() {
        if (dataCenterId < 0 || dataCenterId > BitUtil.max(meta.getDatacenterBits())) {
            throw new RuntimeException(String.format("dataCenterId错误,当前dataCenterId=%d, 合法范围0~%d", dataCenterId,
                    BitUtil.max(meta.getDatacenterBits())));
        }
    }
}
