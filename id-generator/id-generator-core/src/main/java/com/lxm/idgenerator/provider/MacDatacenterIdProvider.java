package com.lxm.idgenerator.provider;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 基于mac地址分配数据中心id
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/11/13
 * @time 11:46
 */
public class MacDatacenterIdProvider implements DatacenterIdProvider {

    private long dataCenterId;
    private long maxDataCenterId = 32;

    /**
     * 使用mac生成datacenterid
     * 参考mybatis-plus的idworker实现
     * @return
     */
    @Override
    public long getDatacenterId() {
        dataCenterId = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                dataCenterId = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    dataCenterId = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    dataCenterId = dataCenterId % (maxDataCenterId + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return dataCenterId;
    }
}
