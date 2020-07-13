package com.lxm.idgenerator.factory;

import com.lxm.idgenerator.bean.IdMeta;
import com.lxm.idgenerator.configuration.AutoConfiguration;
import com.lxm.idgenerator.enums.IdType;
import com.lxm.idgenerator.impl.IdServiceImpl;
import com.lxm.idgenerator.provider.*;
import com.lxm.idgenerator.service.bean.Id;
import com.lxm.idgenerator.service.intf.IdService;
import com.lxm.idgenerator.zookeeper.ZookeeperHelperProxy;
import com.lxm.idgenerator.zookeeper.ZookeeperHandlerImpl;
import com.lxm.idgenerator.zookeeper.ZookeeperStateListener;

import java.text.SimpleDateFormat;

/**
 * IdService bean factory
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/6/28
 * @time 11:17
 */
public class IdServiceBeanFactory {

    /**
     * 获取默认的IdService,idtype为毫秒级, workerId默认为0, dataCenterId默认为0
     * @return
     */
    public static IdService getService() {
        return getService(0L, 0L, IdType.MILLISECOND);
    }

    /**
     * 通过指定的dataCenterId, workerId获取IdService, 默认idtype为毫秒级
     * @param workerId
     * @return
     */
    public static IdService getService(long dataCenterId, long workerId) {
        return getService(dataCenterId, workerId, IdType.MILLISECOND);
    }

    /**
     * 通过指定的dataCenterId,workerId和Id类型获取IdService
     * @param dataCenterId
     * @param workerId
     * @param idType
     * @return
     */
    public static IdService getService(long dataCenterId, long workerId, IdType idType) {
        DefaultWorkerIdProvider defaultWorkerIdProvider = new DefaultWorkerIdProvider();
        defaultWorkerIdProvider.setWorkerId(workerId);
        DatacenterIdProvider datacenterIdProvider = null;
        if (dataCenterId == -1) {
            datacenterIdProvider = new MacDatacenterIdProvider();
        } else {
            datacenterIdProvider = new DefaultDatacenterIdProvider();
            datacenterIdProvider.setDatacenterId(dataCenterId);
        }
        IdServiceImpl idServiceImpl = new IdServiceImpl(datacenterIdProvider, defaultWorkerIdProvider, idType);
        return idServiceImpl;
    }

    /**
     * 根据配置生成idservice
     * @param configuration
     * @return
     */
    public static IdService getService(AutoConfiguration configuration) {

        DatacenterIdProvider datacenterIdProvider = null;
        if (configuration.getDataCenterId() == null || configuration.getDataCenterId().longValue() == -1) {
            datacenterIdProvider = new MacDatacenterIdProvider();
        } else {
            datacenterIdProvider = new DefaultDatacenterIdProvider();
            datacenterIdProvider.setDatacenterId(configuration.getDataCenterId());
        }

        ZookeeperHelperProxy proxy = new ZookeeperHelperProxy(ZookeeperHandlerImpl.getInstance().configure(configuration));
        ZookeeperWorkerIdProvider zookeeperWorkerIdProvider = new ZookeeperWorkerIdProvider(
                datacenterIdProvider.getDatacenterId(), proxy);
        try {
            zookeeperWorkerIdProvider.register();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Id Service zookeeper 注册id失败：" + e.getMessage());
        }
        IdServiceImpl idServiceImpl = new IdServiceImpl(datacenterIdProvider, zookeeperWorkerIdProvider,
                configuration.getIdType() ? IdType.SECOND : IdType.MILLISECOND);
        ZookeeperStateListener listener = new ZookeeperStateListener(idServiceImpl);
        proxy.addListener(listener);
        return idServiceImpl;
    }
}
