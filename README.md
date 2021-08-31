BudWk(原名NutzWk) 开源企业级Java Web开发框架
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

BudWk-V5 Mini 微服务单应用版本（一个jar或打成war运行），管理后台 jQuery + Vue.js + ElementUI，非常适合个人项目快速开发

* 如果是团队开发或大型项目，推荐使用 BudWk-V7 [https://gitee.com/budwk/budwk](https://gitee.com/budwk/budwk)

* 系统自带多级权限体系、日志系统、缓存系统、定时任务、微信管理、CMS管理、beetl模板语言等基础功能

* 安装必要条件： redis + mysql（支持 MariaDB、Oracle、SqlServer、达梦等）

### 项目启动

* 创建数据库 `budwk_v5_mini` 项目启动时会自动建表初始化数据

* `mvn compile nutzboot:run`  mvn运行 或 IDEA 中右击 MainLauncher 运行

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

## 项目部署

* 内置配置文件启动  `nohup java -jar mini.jar &` 带参数 `-Dnutz.profiles.active=prod`(IDEA 运行时填 `--nutz.profiles.active=prod`) 可加载 application-prod.yaml 文件
* 外置配置文件启动  `nohup java -Dnutz.boot.configure.properties.dir=/data/budwk/ -jar mini.jar &` 此时加载文件夹所有 *.yaml 配置文件

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