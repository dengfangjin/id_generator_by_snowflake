package com.lxm.idgenerator.provider;

/**
 * 默认的工作id提供器, 通过手动的方式赋值
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/6/27
 * @time 11:08
 */
public class DefaultWorkerIdProvider implements WorkerIdProvider {

    private long workerId;

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }
}
