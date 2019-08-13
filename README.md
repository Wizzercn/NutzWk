NutzWk 开源企业级Java Web开发框架
======

[![Build Status](https://travis-ci.org/Wizzercn/NutzWk.png?branch=bootstrap)](https://travis-ci.org/Wizzercn/NutzWk)
[![GitHub release](https://img.shields.io/github/release/Wizzercn/NutzWk.svg)](https://github.com/Wizzercn/NutzWk/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![PowerByNutz](https://img.shields.io/badge/PowerBy-Nutz-green.svg)](https://github.com/nutzam/nutz)

https://nutzwk.wizzer.cn                 演示地址

https://wizzer.cn/donation                捐赠者列表

# 前言

本项目发展自2010年，2012年开始用于商业项目，至今已服务于全国各地公司大大小小数千个项目，行业涉及政务、电商、物联网等，随着个人经验积累及从事行业的不同分别发布了1.0至5.0多个版本，每个版本都是完整运行且完全开源免费的，您可以根据项目规模选择不同版本。本项目案例众多，省厅级项目、市级平台、大数据项目、电商平台、物联网平台等等，issues里有部分案例截图，限于篇幅不一一罗列。

我们有强大的后援 —— Nutz 社区支持  https://nutz.cn  及 Nutz 使用手册 https://nutzam.com/core/nutz_preface.html

### QQ交流群
*  1群: 68428921(已满)
*  2群: 24457628

# 版本说明

本版是 NutzWk v5.x 的微服务单应用版本（删除了Dubbo、Zookeeper 等微服务相关依赖），管理后台Vue，非常适合小微型项目

安装必要条件： redis + mysql（或 MariaDB、Oracle、SqlServer、达梦等）

项目启动：直接运行 MyMainLauncher 即可

* mvn package nutzboot:shade 生成可执行jar包

* 正常启动后访问 http://127.0.0.1:8080/sysadmin 用户名 superadmin 密码 1

