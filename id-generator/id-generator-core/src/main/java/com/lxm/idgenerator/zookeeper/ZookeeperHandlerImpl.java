package com.lxm.idgenerator.zookeeper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lxm.idgenerator.configuration.AutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/10/30
 * @time 9:59
 */
@Slf4j
public class ZookeeperHandlerImpl implements ZookeeperHandler {
    volatile CuratorFramework client;
    AutoConfiguration configuration;
    ConnectionStateListener listener;
    private final Map<String, TreeCache> caches = Maps.newHashMap();

    private static ZookeeperHandlerImpl instance = new ZookeeperHandlerImpl();

    public static ZookeeperHandlerImpl getInstance() {
        return instance;
    }

    private ZookeeperHandlerImpl() {
    }

    private void init() {
        if (!configuration.getEnableZk()) {
            return;
        }
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(configuration.getBaseSleepTimeMilliseconds(),
                configuration.getMaxRetries(), configuration.getMaxSleepTimeMilliseconds());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(configuration.getHost())
                .retryPolicy(retryPolicy).namespace(configuration.getNamespace())
                .sessionTimeoutMs(configuration.getSessionTimeoutMilliseconds())
                .connectionTimeoutMs(configuration.getConnectionTimeoutMilliseconds());

        if (StringUtils.isNotBlank(configuration.getDigest())) {
            builder.authorization("digest", configuration.getDigest().getBytes()).aclProvider(new ACLProvider() {
                public List<ACL> getDefaultAcl() {
                    return ZooDefs.Ids.CREATOR_ALL_ACL;
                }

                public List<ACL> getAclForPath(String s) {
                    return ZooDefs.Ids.CREATOR_ALL_ACL;
                }
            });
        }
        client = builder.build();
        client.start();
        log.info("正在尝试连接Zookeeper...");
        try {
            if (!client.blockUntilConnected(configuration.getMaxSleepTimeMilliseconds() * configuration.getMaxRetries(),
                    TimeUnit.MILLISECONDS)) {
                client.close();
                throw new RuntimeException("连接到Zookeeper失败");
            }
            log.info("连接Zookeeper成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取指定路径下有多少个子节点
     * @param path
     * @return
     */
    public int getChildCount(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            if (stat != null) {
                return stat.getNumChildren();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 返回Zookeeper客户端
     * @return
     */
    public CuratorFramework getClient() {
        if (client == null) {
            synchronized (ZookeeperHandlerImpl.class) {
                if (client == null) {
                    init();
                }
            }
        }
        return client;
    }

    /**
     * 获取指定路径下的子路径
     * @param path
     * @return
     */
    public List<String> getChildPath(String path) {
        try {
            List<String> childPaths = client.getChildren().forPath(path);
            childPaths.stream().sorted(new Comparator<String>() {
                public int compare(String o1, String o2) {
                    return o2.compareTo(o1);
                }
            });
            return childPaths;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    /**
     * 判断指定路径是否存在
     * @param path
     * @return
     * @throws Exception
     */
    public boolean isExisted(String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if (stat == null) {
            return false;
        }
        return true;
    }

    /**
     * 创建或更新节点
     * @param path
     * @param value
     * @return
     */
    public boolean saveOrUpdate(String path, String value) {
        try {
            if (isExisted(path)) {
                client.delete().deletingChildrenIfNeeded().forPath(path);
            }
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, value.getBytes());
            return true;
        } catch (final Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public AutoConfiguration getConfiguration() {
        return configuration;
    }


    public ZookeeperHandlerImpl configure(AutoConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public void addListener(ConnectionStateListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
        client.getConnectionStateListenable().addListener(this.listener);
    }

    /**
     * 移除指定路径
     * @param path
     */
    public void remove(String path) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 关闭与服务端的连接
     */
    public void close() {
        if (client != null) {
            CloseableUtils.closeQuietly(client);
            client = null;
        }
    }

    /**
     * 获取指定路径的值
     * @param path
     * @return
     */
    public String get(String path) {
        try {
            return new String(client.getData().forPath(path));
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
