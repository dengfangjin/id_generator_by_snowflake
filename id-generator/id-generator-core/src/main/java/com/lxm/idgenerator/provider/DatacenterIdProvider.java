package com.lxm.idgenerator.provider;

public interface DatacenterIdProvider {
    /**
     * 获取数据中心id
     * @return
     */
    long getDatacenterId();

    default void setDatacenterId(long id) {}
}
