NutzWk 开源企业级Java Web开发框架
======

[![Build Status](https://travis-ci.org/Wizzercn/NutzWk.png?branch=bootstrap)](https://travis-ci.org/Wizzercn/NutzWk)
[![GitHub release](https://img.shields.io/github/release/Wizzercn/NutzWk.svg)](https://github.com/Wizzercn/NutzWk/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![PowerByNutz](https://img.shields.io/badge/PowerBy-Nutz-green.svg)](https://github.com/nutzam/nutz)

https://nutzwk.wizzer.cn                 V5演示地址

https://demo.budwk.com                 V7演示地址

https://budwk.com                官网

# 前言

本框架自2012年开始用于商业项目，至今已服务于全国各地公司大大小小数千个项目，行业涉及政务、电商、物联网等，随着个人经验积累及从事行业的不同分别发布了1.x至7.x多个版本，您可以根据项目规模选择不同版本。本项目案例众多，省厅级项目、市级平台、大数据项目、电商平台、物联网平台等等。

我们有强大的后援 —— Nutz 社区支持  https://nutz.cn  及 Nutz 使用手册 https://nutzam.com/core/nutz_preface.html

### QQ交流群
*  1群: 24457628
*  2群: 68428921

# 版本说明

NutzWk-V5 Mini 微服务单应用版本（一个jar或打成war运行），管理后台 Vue.js + ElementUI，非常适合项目快速开发

* 系统自带多级权限体系、日志系统、缓存系统、定时任务、微信管理、CMS管理、beetl模板语言等基础功能

* 安装必要条件： redis + mysql（支持 MariaDB、Oracle、SqlServer、达梦等）

### 项目启动

* 创建数据库 `budwk-v5-mini` 直接运行 MainLauncher 即可，启动会自动建表初始化数据


* `mvn compile nutzboot:run`   mvn运行

* `mvn package nutzboot:shade -Dmaven.javadoc.skip=true -Dmaven.test.skip=true` 生成可执行jar包

* `mvn clean package nutzboot:shade nutzboot:war -Dmaven.javadoc.skip=true -Dmaven.test.skip=true` 生成可执行war包

* 正常启动后访问 http://127.0.0.1:8080/sysadmin 用户名 superadmin 密码 1


# 版本说明

*   v7.x - nacos 微服务网关+组件化+API化版本 ```前后端分离,前端 nuxt + vue + elementUI```
*   v6.x - nacos 微服务分布式版本 ```前后端分离,前端 nuxt + vue + elementUI```
*   v6.x - zookeeper 微服务分布式版本 ```前后端分离,前端 nuxt + vue + elementUI```
*   v6.x - mini 微服务单应用版本（一个 jar 或 war 包） ```前后端分离,前端 nuxt + vue + elementUI```
*   v5.x - zookeeper 微服务分布式版本 ```前端 jQuery + bootsrtap 或 jQuery + vue.js + elementUI```
*   v5.x - mini 微服务单应用版本（一个 jar 或 war 包） ```前端 jQuery + bootsrtap 或 jQuery + vue.js + elementUI```
*   v4.x - 单应用版本（war 包） ```前端 jQuery + bootsrtap```
*   v3.x - 单应用版本（war 包） ```前端 jQuery + bootsrtap```
*   v1.x - 单应用版本（war 包）  ```前端 jQuery + easyUI```


| 版本名称 | 版本特点 | 版本地址 | 运行方式 | 后端主要技术| 前端主要技术 | 浏览器兼容性 |
| ---------|---------| ----------| ----------| ----------|----------|----------|
| BudWk v7.x | 微服务网关+组件化+API化 + 前后端分离 |[v7.x](https://github.com/budwk/budwk/tree/v7.x)| jar,war | nutzboot + dubbo + nacos  | nuxt + vue + elementUI | Chrome,Edge,IE12+ |
| BudWk v6.x-nacos | 微服务分布式 + 前后端分离 |[v6.x-nacos](https://github.com/budwk/budwk/tree/v6.x-nacos)| jar,war | nutzboot + dubbo + nacos + shiro | nuxt + vue + elementUI | Chrome,Edge,IE12+ |
| BudWk v6.x-zookeeper | 微服务分布式 + 前后端分离 |[v6.x-zookeeper](https://github.com/budwk/budwk/tree/v6.x-zookeeper)| jar,war | nutzboot + dubbo + zookeeper + shiro | nuxt + vue + elementUI | Chrome,Edge,IE12+ |
| BudWk v6.x-mini | 微服务单应用 + 前后端分离 |[v6.x-mini](https://github.com/budwk/budwk/tree/v6.x-mini)| jar,war | nutzboot + shiro | nuxt + vue + elementUI | Chrome,Edge,IE12+ |
| NutzWk v5.x| 微服务分布式 + 前端混合模式 |[v5.x](https://github.com/Wizzercn/NutzWk/tree/v5.x)| jar,war | nutzboot + dubbo + shiro + beetl | vue + elementUI + jquery 或 jquery + bootstrap 两个版本 | Chrome,IE9+ |
| NutzWk v5.x-mini| 微服务单应用 + 前端混合模式 |[v5.x-mini](https://github.com/Wizzercn/NutzWk/tree/v5.x-mini)| jar,war | nutzboot + shiro + beetl | vue + elementUI + jquery | Chrome,IE9+ |
| NutzWk v4.x| 模块化单应用 |[v4.x](https://github.com/Wizzercn/NutzWk/tree/v4.x)| war | nutz + shiro + beetl | jquery + bootstrap | Chrome,IE7 + |
| NutzWk v3.x| 单应用 |[v3.x](https://github.com/Wizzercn/NutzWk/tree/v3.x)| war | nutz + shiro + beetl 或 velocity 两个版本 | jquery + bootstrap | Chrome,IE7 + |
| NutzWk v1.x| 单应用 |[v1.x](https://github.com/Wizzercn/NutzWk/tree/v1.x)| war | nutz + shiro + velocity | jquery + easyUI | IE6 + |



## 项目部署

* 内置配置文件启动  `nohup java -jar mini.jar &` 带参数 `-Dnutz.profiles.active=prod` 可加载 application-prod.properties 文件
* 外置配置文件启动  `nohup java -Dnutz.boot.configure.properties.dir=/data/budwk/ -jar mini.jar &` 此时加载文件夹所有 *.properties 配置文件
* 生产环境可以使用 [PythonWk](https://github.com/Wizzercn/PythonWk) 进行部署,登陆后台运维中心可在线更新jar包及配置文件等

# 鸣谢

* [@wendal](https://github.com/wendal)
* [@rekoe](https://github.com/Rekoe)
* [@enilu](https://github.com/enilu)
* [@loyalove](https://github.com/loyalove)
* [@threefish](https://github.com/threefish)

# 关于

* 个人提供付费的培训服务，含源码解析、设计思路、疑难解答、项目辅导等
* 联系方式 QQ：11624317  微信：wizzer
* 欢迎打赏，以资鼓励 [https://budwk.com/donation](https://budwk.com/donation)