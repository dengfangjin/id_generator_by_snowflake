package com.lxm.idgenerator.zookeeper;

import com.lxm.idgenerator.util.IpUtil;
import lombok.Data;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

/**
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/11/1
 * @time 8:55
 */
@Data
public class ZkNode {

    private String ip;
    private String hostName;
    private String pid;
    private Long sessionId;
    private Integer workerId;

    public ZkNode(Long sessionId, Integer workerId) {
        this.sessionId = sessionId;
        this.workerId = workerId;
        this.ip = IpUtil.getIpAddress().getHostAddress();
        this.hostName = IpUtil.getIpAddress().getHostName();
        this.pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
}
