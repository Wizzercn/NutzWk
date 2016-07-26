NutzWk 基于Nutz的开源企业级开发框架 
======

[![Build Status](https://travis-ci.org/Wizzercn/NutzWk.png?branch=bootstrap)](https://travis-ci.org/Wizzercn/NutzWk)
[![GitHub release](https://img.shields.io/github/release/Wizzercn/NutzWk.svg)](https://github.com/Wizzercn/NutzWk/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

开发计划:

* NutzWk 4.0.x   前后端分离版 (vue + nutz) **筹备中..**
* NutzWk 3.2.x   CMS+ (beetl / beetl+velocity)
* NutzWk 3.1.x   微信+ (beetl / beetl+velocity)
* NutzWk 3.0.x   纯净版(beetl / beetl+velocity)
* NutzWk 2.0.x   试验版(velocity)
* NutzWk 1.0.x   传统版(velocity 支持IE6)

注:_velocity后缀为同时支持beetl和velocity两种模板引擎版本  

交流群: 68428921

在线演示地址
======
http://nutzwk.wizzer.cn/                 NutzWk v3.x

https://github.com/Wizzercn/NodeWk       Node.js版源码

======
基于Nutz的开源企业级开发框架

NutzWk 3.x 运行环境：
*   JDK 8
*   Maven 3.3.3
*   Nutz v1.r.57

NutzWk 3.x 新特性：
*   集成Shiro权限框架
*   集成Ehcache缓存
*   集成Redis
*   支持语言国际化
*   支持注解式事务
*   支持动作链
*   支持注解式日志
*   支持插件式加载
*   集成Email服务
*   集成Quartz定时任务
*   集成Lucene搜索引擎
*   集成Beetl/Velocity模板引擎
*   后台管理界面采用Pjax+Bootstrap
*   自定义路由


使用说明：
*   创建空的数据库
*   修改数据库连接 /resources/config/custom/db.properties
*   项目使用Maven构建，IDEA/Eclipse直接打开，等待包下载完毕
*   启动时自动建表
*   用户名：superadmin  密码：1

======
和 NutzWk 1.0 的主要区别：
*   集成Shiro，更加完善的权限体系
*   界面使用Bootstrap，PJAX加载
*   MVC结构，事务支持更佳
*   注解式日志，企业级业务操作记录更方便
*   DataTables等JS插件的使用
*   国际化字符串
*   ……

代码生成器
======
https://github.com/Wizzercn/NutzCodematic/tree/v3.0.0       适用于NutzWk v3.x（可视化界面,通过数据表生成代码及页面）

https://github.com/enilu/nutzwk-code-generator              适用于NutzWk v3.x（Maven插件,通过实体类生成代码及页面）

```
  <dependency>
        <groupId>cn.enilu</groupId>
        <artifactId>nutzwk-code-generator</artifactId>
        <version>1.0</version>
    </dependency>
```

https://github.com/Wizzercn/NutzCodematic/tree/v1.0.0       适用于NutzWk 1.0

在线演示地址
======

https://nutzwk.nutz.cn/                 NutzWk 2.0

https://wzflow.nutz.cn/          NutzWk 1.0 (含Activiti工作流)

======

NutzWk 1.0 源码是这个链接： https://github.com/Wizzercn/NutzWk/tree/1.0

NutzWk 1.0 含Activiti工作流的源码在这里： https://github.com/wendal/wzflow

NutzWk v3.x 运行截图
======
![主界面截图](nutzwk_home.png)
