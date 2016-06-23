NutzWk 3.0 纯净版（开发中...完成时会打tag）
======
目录结构调整较大，和2.0不兼容，所以起名叫3.0。

主要是把过去一年Node.js开发中积累的一些经验用到此版本中，特别是后台界面功能。

PS：如果对Node.js开发框架感兴趣，可以看这里：http://www.nodeshop.cn/sysadmin

======
基于Nutz的开源企业级开发框架

NutzWk 3.0 运行环境：
*   JDK 8
*   Maven 3.3.39
*   Nutz v1.r.57

NutzWk 3.0 新特性：
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
*   集成Beetl模板引擎
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

在线演示地址
======

https://nutzwk.nutz.cn/                 NutzWk 2.0

https://wzflow.nutz.cn/          NutzWk 1.0 (含Activiti工作流)

======

NutzWk 1.0 源码是这个链接： https://github.com/Wizzercn/NutzWk/tree/1.0

NutzWk 1.0 含Activiti工作流的源码在这里： https://github.com/wendal/wzflow

