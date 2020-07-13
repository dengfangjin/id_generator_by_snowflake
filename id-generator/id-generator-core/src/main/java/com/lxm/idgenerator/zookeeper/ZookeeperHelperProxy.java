package com.lxm.idgenerator.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionStateListener;

import java.util.List;

/**
 * zookeeper辅助类代理
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/10/30
 * @time 13:36
 */
public class ZookeeperHelperProxy implements ZookeeperHandler {
    private ZookeeperHandlerImpl helper;

    public ZookeeperHelperProxy(ZookeeperHandlerImpl helper) {
        this.helper = helper;
    }

    public int getChildCount(String path) {
        validateConfigure();
        return helper.getChildCount(path);
    }

    public CuratorFramework getClient() {
        validateConfigure();
        return helper.getClient();
    }

    public List<String> getChildPath(String path) {
        validateConfigure();
        return helper.getChildPath(path);
    }

    public boolean isExisted(String path) throws Exception {
        validateConfigure();
        return helper.isExisted(path);
    }

    public boolean saveOrUpdate(String path, String value) {
        validateConfigure();
        return helper.saveOrUpdate(path, value);
    }

    public void addListener(ConnectionStateListener listener) {
        validateConfigure();
        helper.addListener(listener);
    }

    public String get(String path) {
        validateConfigure();
        return helper.get(path);
    }

    private void validateConfigure() {
        if (helper.getConfiguration() == null) {
            throw new RuntimeException("Zookeeper配置未初始化");
        }
    }

    public void remove(String path) {
        helper.remove(path);
    }

    public void close() {
        helper.close();
    }
}
