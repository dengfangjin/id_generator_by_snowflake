package com.lxm.idgenerator.zookeeper;

import com.lxm.idgenerator.service.intf.IdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * zookeeper状态监听器
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/10/30
 * @time 14:37
 */
@Slf4j
public class ZookeeperStateListener implements ConnectionStateListener {

    private IdService idService;

    public ZookeeperStateListener(IdService idService) {
        this.idService = idService;
    }

    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        switch (newState) {
            case LOST:
                refreshWorkerId();
                break;
            case SUSPENDED:
                refreshWorkerId();
                break;
            default:
                break;
        }
    }

    void refreshWorkerId() {
        log.warn("Zookeeper连接已断开,将重新获取workerId");
        this.idService.refreshWorkerId();
    }
}
