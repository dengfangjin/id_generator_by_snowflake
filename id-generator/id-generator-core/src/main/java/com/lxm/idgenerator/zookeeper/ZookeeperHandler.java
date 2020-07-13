package com.lxm.idgenerator.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionStateListener;

import java.util.List;

public interface ZookeeperHandler {
    /**
     * 获取指定路径下有多少个子节点
     * @param path
     * @return
     */
    int getChildCount(String path);

    /**
     * 返回Zookeeper客户端
     * @return
     */
    CuratorFramework getClient();

    /**
     * 获取指定路径下的子路径
     * @param path
     * @return
     */
    List<String> getChildPath(String path);

    /**
     * 判断指定路径是否存在
     * @param path
     * @return
     * @throws Exception
     */
    boolean isExisted(String path) throws Exception;

    /**
     * 创建或更新节点
     * @param path
     * @param value
     * @return
     */
    boolean saveOrUpdate(String path, String value);

    /**
     * 添加监听
     * @param listener
     */
    void addListener(ConnectionStateListener listener);

    /**
     * 移除指定路径
     * @param path
     */
    void remove(String path);

    /**
     * 关闭与服务端的连接
     */
    void close();

    /**
     * 获取指定路径的值
     * @param path
     * @return
     */
    String get(String path);
}
