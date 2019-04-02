NutzWk 开源企业级Java Web开发框架
======

[![Build Status](https://travis-ci.org/Wizzercn/NutzWk.png?branch=bootstrap)](https://travis-ci.org/Wizzercn/NutzWk)
[![GitHub release](https://img.shields.io/github/release/Wizzercn/NutzWk.svg)](https://github.com/Wizzercn/NutzWk/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![PowerByNutz](https://img.shields.io/badge/PowerBy-Nutz-green.svg)](https://github.com/nutzam/nutz)

https://nutzwk.wizzer.cn                 演示地址

https://wizzer.cn/donation                捐赠者列表

# 前言

本项目发展自2010年，2012年开始用于商业项目，至今已服务于全国各地公司大大小小数百个项目，行业涉及政务、电商、物联网等，随着个人经验积累及从事行业的不同分别发布了1.0至5.0多个版本，每个版本都是完整运行且完全开源免费的，您可以根据项目规模选择不同版本。本项目案例众多，省厅级项目、市级平台、大数据项目、电商平台、物联网平台等等，issues里有部分案例截图，限于篇幅不一一罗列。

我们有强大的后援 —— Nutz 社区支持  https://nutz.cn  及 Nutz 使用手册 https://nutzam.com/core/nutz_preface.html

### QQ交流群
*  1群: 68428921
*  2群: 24457628

# 版本说明

*   NutzWk v5.x 微服务版本(分支名:[nutzboot-dubbo](https://github.com/Wizzercn/NutzWk/tree/nutzboot-dubbo),微服务dubbo分布式版本)
*   NutzWk v4.x 模块化版本(分支名:[modular](https://github.com/Wizzercn/NutzWk/tree/modular),统一提供代码生成器及IDEA可视化插件)
*   NutzWk v3.x 单应用版本(分支名:[bootstrap-3.3.x](https://github.com/Wizzercn/NutzWk/tree/bootstrap-3.3.x),CMS+微信+系统+权限+常用功能封装 beetl/velocity)
*   NutzWk v1.0 传统版(分支名:[master](https://github.com/Wizzercn/NutzWk/tree/master),velocity 支持IE6)


# 本版说明(v5.x)

## NutzWk 5.x 运行必备环境：

*   JDK 8 181 + 或 OpenJDK 11 +
*   Maven 3.5.3 +
*   Redis 4.0.8 +
*   MySql 5.7 + 或 MariaDB、Oracle、SqlServer、达梦等
*   Zookeeper 3.4.11 +

## NutzWk 5.x 技术选型：

*   核心框架：Nutzboot
*   分布式框架：Dubbo(RPC)、Zookeeper(注册中心)、Sentinel(流控)、Fescar(分布式事务)
*   安全框架：Shiro、JWT
*   任务调度：Quartz
*   数据库连接池：Druid 
*   支持数据库：MySql、MariaDB、Oracle、SqlServer、达梦等
*   缓存框架：Redis、Ehcache、Wkcache
*   订阅发布：Redis
*   文件系统：Ftp(默认)、FastDfs等
*   可扩展功能：WebSocket-Nutz、消息队列-Rabbitmq、搜索引擎-Elasticsearch、工作流-Activiti等
*   前端框架：Bootstrap+JQuery 或 Vue +Element (推荐)

## NutzWk 5.x 使用说明：

| 名称                                     | 介绍                                     |
| ---------------------------------------- | ---------------------------------------- |
|[wk-framework](wk-framework) |一些基类及公共方法的封装|
|[wk-model](wk-app/wk-model) |POJO类|
|[wk-common](wk-app/wk-common) |接口类|
|[wk-nb-service-sys](wk-app/wk-nb-service-sys) |系统管理模块,dubbo服务端,NB项目,权限体系|
|[wk-nb-service-cms](wk-app/wk-nb-service-cms) |CMS管理模块,dubbo服务端,NB项目,ig及wkcache演示|
|[wk-nb-service-wx](wk-app/wk-nb-service-wx) |微信管理模块,dubbo服务端,NB项目,微信及微信支付功能演示|
|[wk-nb-task](wk-app/wk-nb-task) |定时任务模块,dubbo服务端,NB项目,支持quartz集群|
|[wk-nb-web-api](wk-app/wk-nb-web-api) |API JWT Token示例,dubbo消费端,NB项目,Mvc|
|[wk-nb-web-platform](wk-app/wk-nb-web-platform) |WEB管理后台(可选),dubbo消费端,NB项目,Mvc|
|[wk-nb-web-vue](wk-app/wk-nb-web-vue) |WEB管理后台Vue.js混合版(可选),dubbo消费端,NB项目,Mvc|

![models](wk-wiki/images/08.png)

*   确保 MySql、Redis、Zookeeper 默认端口配置并已启动好
*   MySql 创建名为 `nutzwk_nb` 的空数据库,在每个NB(nutzboot缩写)模块启动时会自动建表,同时初始化数据
*   项目根目录执行 `mvn clean install -Dmaven.test.skip=true`
*   在单个NB模块下执行 `mvn compile nutzboot:run` 运行或 `mvn package nutzboot:shade` 生成可执行jar包
*   在项目根目录执行 `mvn -Dnutzboot.dst=E:/dst clean package nutzboot:shade` 可将所有可运行jar包生成到指定位置
*   启动顺序是 sys --> cms[可选] --> wx[可选] --> task[可选] --> web-platform 或 web-vue --> web-api[可选]
*   正常启动后访问 `http://127.0.0.1:8080/sysadmin` 用户名 superadmin 密码 1
*   框架详细介绍及代码生成器的使用等内容请仔细阅读 [wk-wiki](wk-wiki)
*   若觉得项目复杂上手较难,可以从最简单的一个NB项目学起 [wizzer.cn 源码](https://github.com/Wizzercn/Demo/tree/master/nutzboot-wizzer-cn)

### 项目部署

*   内置配置文件启动  `nohup java -jar wk-nb-service-sys.jar &` 带参数 `-Dnutz.profiles.active=prod` 可加载 application-prod.properties 文件
*   外置配置文件启动  `nohup java -Dnutz.boot.configure.properties.dir=/data/nutzwk/sys/ -jar wk-nb-service-sys.jar &` 此时加载文件夹所有 *.properties 配置文件
*   生产环境可以使用 [PythonWk](https://github.com/Wizzercn/PythonWk) 进行部署,登陆后台运维中心可在线更新jar包及配置文件等


### 分布式事务

*   业务走过的链路所有NB模块, pom.xml 添加
    ```xml
    <dependency>
        <groupId>org.nutz</groupId>
        <artifactId>nutzboot-starter-fescar</artifactId>
    </dependency>
    <dependency>
      <groupId>com.alibaba.fescar</groupId>
      <artifactId>fescar-dubbo-alibaba</artifactId>
      <version>${fescar.version}</version>
    </dependency>  
    ```
*   业务走过的链路所有NB模块, 配置文件中添加
    ```text
    fescar.enabled=true
    # fescar.applicationId 在本项目中会自动获取无需赋值
    # fescar.applicationId=
    fescar.txServiceGroup=my_test_tx_group
    ```
*   下载并启动 [fescar服务端](https://github.com/alibaba/fescar/releases) 
*   业务方法上加上注解 `@GlobalTransactional` 即可,可选参数 `timeoutMills = 300000, name = "my_test"`
*   与本地事务注解 `@Aop(TransAop.READ_COMMITTED)` 不冲突
*   业务方法内不要加 try catch (与本地事务注解一样)要让异常抛出来事务才能工作
*   分布式事务不是越多越好,可以在核心业务如交易环节增加,建议实现乐观锁来预防脏数据产生

# 鸣谢
*   [@wendal](https://github.com/wendal) (代码贡献者,技术大牛,Nutz主要作者,无所不知且乐于助人)
*   [@rekoe](https://github.com/Rekoe) (代码贡献者)
*   [@enilu](https://github.com/enilu) (3.x 代码生成器及IDEA插件贡献者)
*   [@loyalove](https://github.com/loyalove) (3.x Vue代码贡献者)
*   [@threefish](https://github.com/threefish) (控制类快速定位模板页面IDEA插件贡献者)
*   以及交流群里热心的小伙伴们~ QQ交流群: 24457628


# 关于

*   **本项目完全开源，商用完全免费**
*   推荐商业用户打赏500￥以上，支持项目持续发展，以及得到更好的技术支持
*   另外提供付费的培训服务，含源码解析、设计思路、疑难解答、项目辅导等
*   联系方式 QQ：11624317  微信：wizzer
*   欢迎打赏，以资鼓励 [https://wizzer.cn/donation](https://wizzer.cn/donation)

## Credits

### Contributors

This project exists thanks to all the people who contribute. 

<a href="graphs/contributors"><img src="https://opencollective.com/nutzwk/contributors.svg?width=890&button=false" /></a>

### Backers

Thank you to all our backers! 🙏 [[Become a backer](https://opencollective.com/nutzwk#backer)]

<a href="https://opencollective.com/nutzwk#backers" target="_blank"><img src="https://opencollective.com/nutzwk/backers.svg?width=890"></a>

### Sponsors

Support this project by becoming a sponsor. Your logo will show up here with a link to your website. [[Become a sponsor](https://opencollective.com/nutzwk#sponsor)]

<a href="https://opencollective.com/nutzwk/sponsor/0/website" target="_blank"><img src="https://opencollective.com/nutzwk/sponsor/0/avatar.svg"></a>

