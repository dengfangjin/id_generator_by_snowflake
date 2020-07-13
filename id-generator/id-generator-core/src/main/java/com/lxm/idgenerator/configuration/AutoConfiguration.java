package com.lxm.idgenerator.configuration;

import com.lxm.idgenerator.enums.IdType;
import com.lxm.idgenerator.factory.IdServiceBeanFactory;
import com.lxm.idgenerator.service.intf.IdService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/6/28
 * @time 9:56
 */
@Configuration
public class AutoConfiguration {

    /**
     * 是否启用zookeeper管理workerId, 默认false
     */
    @Value("${id.zookeeper.enable:false}")
    private Boolean enableZk;

    /**
     * ZooKeeper连接信息, 格式<code>主机名</code>:<code>端口</code>。多个主机用英文逗号分隔
     * 如:192.168.1.1:2181,192.168.1.2:2181
     */
    @Value("${id.zookeeper.serverLists:null}")
    private String host;

    /**
     * Zookeeper名字空间
     */
    @Value("${id.zookeeper.namespace:id-generator}")
    private String namespace;


    /**
     * 等待重试的间隔时间. 单位毫秒. 默认1s
     */
    @Value("${id.zookeeper.baseSleepTime:1000}")
    private Integer baseSleepTimeMilliseconds;

    /**
     * 等待重试的最大值间隔时间. 单位毫秒. 默认3s
     */
    @Value("${id.zookeeper.maxSleepTime:3000}")
    private Integer maxSleepTimeMilliseconds;

    /**
     * 最大重试次数. 默认3次
     */
    @Value("${id.zookeeper.maxRetries:3}")
    private Integer maxRetries;

    /**
     * zookeeper身份校验
     */
    @Value("${id.zookeeper.digest:null}")
    private String digest;

    /**
     * 会话超时时间. 单位毫秒. 默认60s
     */
    @Value("${id.zookeeper.sessionTimeout:60000}")
    private Integer sessionTimeoutMilliseconds;

    /**
     * 连接超时时间. 单位毫秒. 默认15s
     */
    @Value("${id.zookeeper.connectionTimeout:15000}")
    private Integer connectionTimeoutMilliseconds;


    /**
     * 直接指定机器码，一个数据中心最多可同时部署32台
     * 范围0-31
     */
    @Value("${id.workerId:0}")
    private Long workerId;

    /**
     * id类型, true-启用秒级别 false-启用毫秒级别, 默认为false即毫秒级别
     */
    @Value("${id.type.second:false}")
    private Boolean idType;

    /**
     * 指定数据中心, 最多同时部署32个数据中心
     * 范围0-31
     */
    @Value("${id.datacenterId:-1}")
    private Long dataCenterId;

    public static AutoConfiguration getDefaultZookeeperConfigure(String host, String digest, boolean isSecond) {
        AutoConfiguration configuration = new AutoConfiguration();
        configuration.setEnableZk(true);
        configuration.setIdType(isSecond);
        configuration.setNamespace("id-generator");
        configuration.setConnectionTimeoutMilliseconds(15000);
        configuration.setSessionTimeoutMilliseconds(60000);
        configuration.setMaxRetries(3);
        configuration.setMaxSleepTimeMilliseconds(3000);
        configuration.setBaseSleepTimeMilliseconds(1000);
        configuration.setHost(host);
        if (StringUtils.isNotBlank(digest)) {
            configuration.setDigest(digest);
        }
        return configuration;
    }

    @Bean
    public IdService idService() {
        if (enableZk == null || !enableZk) {
            return IdServiceBeanFactory.getService(dataCenterId == null ? -1L : dataCenterId,
                    workerId, IdType.parse(idType));
        }
        return IdServiceBeanFactory.getService(this);
    }

    public Boolean getEnableZk() {
        return enableZk;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public Integer getBaseSleepTimeMilliseconds() {
        return baseSleepTimeMilliseconds;
    }

    public void setBaseSleepTimeMilliseconds(Integer baseSleepTimeMilliseconds) {
        this.baseSleepTimeMilliseconds = baseSleepTimeMilliseconds;
    }

    public Integer getMaxSleepTimeMilliseconds() {
        return maxSleepTimeMilliseconds;
    }

    public void setMaxSleepTimeMilliseconds(Integer maxSleepTimeMilliseconds) {
        this.maxSleepTimeMilliseconds = maxSleepTimeMilliseconds;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getSessionTimeoutMilliseconds() {
        return sessionTimeoutMilliseconds;
    }

    public void setSessionTimeoutMilliseconds(Integer sessionTimeoutMilliseconds) {
        this.sessionTimeoutMilliseconds = sessionTimeoutMilliseconds;
    }

    public Integer getConnectionTimeoutMilliseconds() {
        return connectionTimeoutMilliseconds;
    }

    public void setConnectionTimeoutMilliseconds(Integer connectionTimeoutMilliseconds) {
        this.connectionTimeoutMilliseconds = connectionTimeoutMilliseconds;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Boolean getIdType() {
        return idType;
    }

    public void setIdType(Boolean idType) {
        this.idType = idType;
    }

    public String getDigest() {
        return digest;
    }

    public void setEnableZk(Boolean enableZk) {
        this.enableZk = enableZk;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }
}
