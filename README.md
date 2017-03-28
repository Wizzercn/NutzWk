NutzWk 基于Nutz的开源企业级开发框架
======

[![Build Status](https://travis-ci.org/Wizzercn/NutzWk.png?branch=bootstrap)](https://travis-ci.org/Wizzercn/NutzWk)
[![GitHub release](https://img.shields.io/github/release/Wizzercn/NutzWk.svg)](https://github.com/Wizzercn/NutzWk/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

在线演示地址
======
https://nutzwk.wizzer.cn/                 NutzWk v3.x、v4.x

https://vue.wizzer.cn                     NutzWk v3.x-vue

# 前言

项目源于2010年，那时老东家还在使用Jsp和Struts1，需要手动创建并释放连接池、需要配置XML请求路径和类映射关系、要支持刚刚兴起的JSON非常痛苦等等的原因，开始选择适用“快速开发、功能丰富、扩展性强、性能优越”等技术要求的框架产品，讨厌Spring的繁杂配置、Hibernate及Mybatis的繁琐，讨厌一切让开发变得低效和繁杂的技术，这和Nutz的设计理念不谋而合。

使用本框架开发商用项目始于2012年，先是基于NutzWk v1.0开发了CMS网站群管理系统、网络问政系统，而后分别用于交通厅网站群项目、12345市长热线项目、财政厅数据上报、羽毛球场地管理、新媒体指数等项目，3.x应用于Police大数据分析、Police视频监控平台及中科大财务处、天乐e生活等各类微信公众号项目中，经过几年的积累，使用NutzWk开发并商用的项目少则几十多则上百。因为她是开源的，不光老东家和现所在公司在用，广大网友也在用哦。

我们有强大的后援 —— Nutz 社区支持  https://nutz.cn  及 Nutz 使用手册 https://nutzam.com/core/nutz_preface.html

### QQ交流群: 68428921

ps：这几年明显感受到国产开源项目越来越多，各种五花八门的框架，让使用者很难选择；不鼓吹自己的框架多么多么好，个人觉得适用于项目需要并且能在满足技术需求的基础上最大限度的提高开发效率的框架，就是好框架。

# 版本说明

*   NutzWk v4.x 模块化版本(统一提供代码生成器及IDEA可视化插件)
*   NutzWk v3.x 单应用版本(CMS+微信+系统+权限+常用功能封装 beetl/velocity)
*   NutzWk v2.0 试验版(不建议使用)
*   NutzWk v1.0 传统版(velocity 支持IE6)

如果您的项目面对的客户比较念旧，可以选用v1.0版本，因为她使用EasyUI以及支持IE6；

如果您的项目需要快速开发，并习惯通过负载均衡方式提升性能，可以选用v3.x版本，她采用Bootstrap+JQuery+Json开发；

如果您的项目有大数据及大并发、分布式部署等需求，那么您可以选用v4.x版本，她在v3.x基础上拆分模块并接口化；

## NutzWk v4.x主分支

https://github.com/Wizzercn/NutzWk/tree/modular   模块化支持分布式部署

## NutzWk v3.x主分支

https://github.com/Wizzercn/NutzWk/tree/bootstrap-3.3.x   单应用快速开发, 功能与v4.x同步升级

# 本版说明(v4.x)

## NutzWk 4.x 运行环境：

*   JDK 8
*   Tomcat 8.x + / Jetty 9.x +
*   Maven 3.3.x +

## NutzWk 4.x 新特性：

*   模块化可自由拆分(Dubbo/RSF自由选择)
*   集成Shiro权限框架(支持二级缓存)
*   集成Ehcache缓存(Shiro一级缓存)
*   集成Redis(支持Redis集群模式切换)
*   集成Email服务(仅供选择)
*   集成Quartz定时任务(集群部署咨询作者)
*   集成Beetl/Velocity模板引擎
*   支持语言国际化(直接写汉字不需要Unicode转换)
*   支持注解式事务(基于强大的@Aop注解)
*   支持动作链配置(想加过滤器So easy)
*   支持注解式日志(@SLog注解自动记录日志)
*   支持自定义路由(显性转发或隐性转发)
*   支持class或jar插件热插拔(单机部署模式)
*   支持API Token及应用管理(结合Nodejs一键生成API DOC)
*   后台管理界面采用Pjax+Bootstrap
*   系统模块(单位、角色、用户、菜单等完整的权限体系)
*   CMS模块(简易的内容管理功能)
*   微信模块(支持多公众号、微信支付等功能)


## NutzWk 4.x 使用说明：

*   创建空的数据库
*   修改数据库连接 wk-app/wk-web/src/main/resources/config/custom/db.properties
*   项目使用Maven构建，IDEA/Eclipse直接打开，等待包下载完毕
*   启动wk-web项目时自动建表
*   http://127.0.0.1/sysadmin
*   用户名：superadmin 密码：1

## NutzWk 4.x 代码结构:

>wk-app

   >>wk-web               (v3.x后台功能)

   >>wk-dubbo              (dubbo示例)
      >>>wk-dubbo-api       (dubbo API)
      >>>wk-dubbo-provider  (dubbo Provider)

>wk-code

   >>wk-code-generator    (代码生成器)

   >>wk-code-ideaplugin   (IDEA插件)

>wk-framework            (基础框架)

>wk-wiki                (开发指南)


## NutzWk 4.x 代码生成器安装使用:
*   IDEA Settings --> Plugins --> Install plugin from disk --> wk-code-ideaplugin.jar
*   创建实体类,必须有@Table,若字段需生成到模板页面则需加 @Comment 字段备注
*   编译wk-web项目,使其打包发布至 target/ 目录
*   在实体类上鼠标右击,Code(Alt+Insert/Mouse Right) --> Generate --> wk mvc

![IDEA插件截图](wk-code/wk-code-ideaplugin/demo.png)

# 鸣谢
*   [@wendal](https://github.com/wendal) (代码贡献者,技术大牛,Nutz主要作者,无所不知且乐于助人)
*   [@rekoe](https://github.com/Rekoe) (代码贡献者)
*   [@enilu](https://github.com/enilu) (代码生成器及IDEA插件贡献者)
*   [@loyalove](https://github.com/loyalove)(Vue代码贡献者)
*   以及交流群里热心的小伙伴们~

# 关于

本项目完全开源，商用也是免费的。

欢迎 “打赏” 以资鼓励，服务器续费及开发动力来源于你们的支持~~感谢~

打赏地址：https://nutz.cn/pay/bc

![打赏](apay.png)

或支付宝扫码：

![支付宝](alipay.png)
