# id-generator

#### 项目介绍
基于Twitter的SnowFlake算法实现的分布式ID发号器。支持手动或通过Zookeeper分配workerId。配置简单，操作简易。生成的id具备全局唯一，粗略有序，可反向解码等特性。


#### 数据结构
##### 毫秒级

&nbsp; | 时间位 | 数据中心 | 工作机器 | 序号位
---|---|---|---|--- 
位数 | 41 | 5 | 5 | 12
- 时间41位，可以使用2^41/1000/60/60/24/365=69.73, 约可使用69年
- 数据中心5位, 可部署2^5=32个数据中心
- 工作机器5位，同一个数据中心可部署2^5=32个服务
- 序号12位，表示同一机器同一时间（毫秒)内理论上可以产生最多2^12-1=4095个ID序号

##### 秒级

&nbsp; | 时间位 | 数据中心 | 工作机器 | 序号位
---|---|---|---|--- 
位数 | 31 | 5 | 5 | 22
- 时间31位，可以使用2^31/60/60/24/365=68, 约可使用68年
- 数据中心5位, 可部署2^5=32个数据中心
- 工作机器5位，同一个数据中心可部署2^5=32个服务
- 序号22位，表示同一机器同一时间（秒)内理论上可以产生最多2^22-1=4194303个ID序号

#### 安装

1. 在pom.xml文件中增加以下仓库
```
<repositories>
    <repository>
        <id>iwanttomakemoney_admin</id>
        <url>https://gitee.com/iwanttomakemoney_admin/maven/raw/master/repository</url>
    </repository>
</repositories>
```
引入以下依赖
```
<dependencies>
    <dependency>
		<groupId>com.lxm</groupId>
		<artifactId>id-generator-interface</artifactId>
		<version>2.3</version>
	</dependency>
	<dependency>
		<groupId>com.lxm</groupId>
		<artifactId>id-generator-core</artifactId>
		<version>2.3</version>
	</dependency>
</dependencies>
```


#### 配置

##### 基于spring boot的项目
1. 在yml或property配置文件中设置所需的<a href="#cs" target="_self">参数</a>.（此步骤非必需，若跳过此步骤将生效默认配置）  
2. 在启动类上增加`@EnableIdGenerator`注解即可. 如
```
@SpringBootApplication
@EnableIdGenerator
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
```
##### 普通的spring项目
1. 在类路径下新增一个property配置文件, 设置所需<a href="#cs" target="_self">参数</a>（此步骤非必需，若跳过此步骤将生效默认配置）
2. 在xxxContext.xml文件中增加如下配置以便识别上一步设置的参数
```
<bean id="appProperty"
          class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="locations">
        <array>
            <value>classpath:application.properties</value>
        </array>
    </property>
</bean>
```
3. 新增一个配置类,在其上增加`@EnableIdGenerator`注解即可.如
```
@Configuration
@EnableIdGenerator
public class WebConfigure {
    // other configure 
}
```

##### 非spring项目
在项目中增加一个单例类工具类, 如
```
public class IdUtil {
    private static IdUtil _instance = new IdUtil();
    
    private static IdService idService;

    public static IdService service() {
        return idService;
    }

    private IdUtil() {
        // 使用提供的工厂类生成idService
        idService = IdServiceFactoryBean.idService(new AutoConfiguration());
    }
}
```

#### 使用
在需要的地方注入服务即可使用，如：
```
@Service
class DemoService {
    @Autowired
    private IdService idService;

    public void test() {
        // 通过自动装配提供的服务
        long id1 = idService.genId();
        // 通过单例的形式提供的服务
        long id2 = IdUtil.service().genId();
        ...
    }
}
```

#### API
```
// 生成id
long idService.genId();

// 批量生成id
long[] batchGenId(int count);

// 解析id
Id decode(long id);

// 手动生成id
long encode(long time, long dataCenterId, long workerId, long seq);

// 解析id中的时间戳
Date transTime(long time);
```


#### <span id="cs">参数</span>

属性 | 类型 | 缺省值 | 描述 
---|---|---|---
id.zookeeper.enable | Boolean | false | 是否启用Zookeeper分配workerId。 默认为false表示使用手动分配workerId；若为true则需预先准备至少一个的Zookeeper服务
id.zookeeper.serverLists | String | null | 连接Zookeeper服务器的列表 包括IP地址和端口号 多个地址用逗号分隔如:host1:2181,host2:2181
id.zookeeper.digest | String | null | 连接Zookeeper的权限令牌 缺省为不需要权限验证
id.zookeeper.namespace | String | "id-generator" | Zookeeper的命名空间
id.zookeeper.baseSleepTime | Integer | 1000 | 等待重试的间隔时间的初始值 单位：毫秒
id.zookeeper.maxSleepTime | Integer | 3000 | 等待重试的间隔时间的最大值 单位：毫秒
id.zookeeper.maxRetries | Integer | 3 | 最大重试次数
id.zookeeper.sessionTimeout | Integer | 60000 | 会话超时时间 单位：毫秒
id.zookeeper.connectionTimeout | Integer | 15000 | 连接超时时间 单位：毫秒
id.type.second=false | Boolean | false | true-秒级别 false-毫秒级别 
id.workerId | Integer | 0 | 手动指定工作机器号，当id.zookeeper.enable=false有效
id.datacenterId | Integer | -1 | 手动指定数据中心, 若不指定则将根据mac地址自动分配一个固定的编号



