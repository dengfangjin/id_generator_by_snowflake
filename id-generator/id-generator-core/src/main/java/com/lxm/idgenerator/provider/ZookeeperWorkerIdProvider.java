package com.lxm.idgenerator.provider;

import com.alibaba.fastjson.JSON;
import com.lxm.idgenerator.zookeeper.ZkNode;
import com.lxm.idgenerator.zookeeper.ZookeeperHelperProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 通过Zookeeper注册workerid
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/10/29
 * @time 15:40
 */
@Slf4j
public class ZookeeperWorkerIdProvider implements WorkerIdProvider {
    private long workerId;
    private long dataCenterId;
    private long maxWorker = 32;

    private ZookeeperHelperProxy zkProxy;

    private final String groupName = "/snowflake";
    private String workerPath = "";

    public ZookeeperWorkerIdProvider(long dataCenterId, ZookeeperHelperProxy proxy) {
        this.zkProxy = proxy;
        this.dataCenterId = dataCenterId;
        this.workerPath = groupName + "/" + this.dataCenterId + "/worker";
    }

    /**
     * 向zookeeper注册节点, 获取workerid
     */
    public void register() {
        CuratorFramework cf = zkProxy.getClient();
        if (cf == null) {
            return;
        }
        InterProcessMutex lock = new InterProcessMutex(cf, groupName);
        try {
            int numOfChildren = zkProxy.getChildCount(workerPath);
            if (numOfChildren < maxWorker) {
                if (!lock.acquire(30000, TimeUnit.MILLISECONDS)) {
                    throw new RuntimeException("获取分布式锁失败");
                }
                long sessionId = cf.getZookeeperClient().getZooKeeper().getSessionId();
                List<String> childrenPaths = zkProxy.getChildPath(workerPath);
                for (int workerId = 0; workerId < maxWorker; workerId++) {
                    String workderIdStr = String.valueOf(workerId);
                    String key = workerPath + "/" + workderIdStr;
                    ZkNode currentNode = new ZkNode(sessionId, workerId);
                    // 检查workerId是否有被占用。如果未被占用则新建
                    if (!childrenPaths.contains(workderIdStr)) {
                        zkProxy.saveOrUpdate(key, JSON.toJSONString(currentNode));
                        this.workerId = workerId;
                        return;
                    } else {
                        String value = zkProxy.get(key);
                        ZkNode cacheNode = JSON.parseObject(value, ZkNode.class);
                        // 判断是否为同一应用
                        if (currentNode.equals(cacheNode)) {
                            this.workerId = cacheNode.getWorkerId();
                            return;
                        }
                    }
                }
            }
            throw new RuntimeException("已经达到最大的可用机器数，注册失败");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            try {
                lock.release();
            } catch (Exception ignored) {
                ;
            }
        }
    }

    public void reset() {
        CuratorFramework client = zkProxy.getClient();
        if (client != null) {
            zkProxy.remove(workerPath + "/" + workerId);
            zkProxy.close();
        }
    }

    public long getWorkerId() {
        return workerId;
    }
}
