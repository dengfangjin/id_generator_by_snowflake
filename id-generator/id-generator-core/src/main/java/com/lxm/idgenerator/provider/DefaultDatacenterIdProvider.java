package com.lxm.idgenerator.provider;

/**
 * 默认的数据中心id提供器, 通过手动的方式赋值
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/11/13
 * @time 11:42
 */
public class DefaultDatacenterIdProvider implements DatacenterIdProvider {
    private long dataCenterId;

    @Override
    public long getDatacenterId() {
        return dataCenterId;
    }

    @Override
    public void setDatacenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }
}
