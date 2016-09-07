NutzWk 4.0 开源企业级开发框架(Vue + Nutz)
======

[![Build Status](https://travis-ci.org/Wizzercn/NutzWk.png?branch=bootstrap)](https://travis-ci.org/Wizzercn/NutzWk)
[![GitHub release](https://img.shields.io/github/release/Wizzercn/NutzWk.svg)](https://github.com/Wizzercn/NutzWk/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


交流群: 68428921

在线演示地址
======
https://nutzwk.wizzer.cn/                 NutzWk v3.x

https://vue.wizzer.cn/                 NutzWk v4.x

运行环境
======
Vue

*   node v6.5.x

NutzWk v4.x

*   JDK 8
*   Tomcat 8
*   Maven 3.3.9
*   Nutz v1.r.58

部署说明
======
1、编译Vue前端：
*   >cd src/vue-admin
*   >npm i
*   >npm run build

2、启动 NutzWk 后端：
*   创建空的数据库
*   修改数据库连接 /resources/config/custom/db.properties
*   项目使用Maven构建，IDEA/Eclipse直接打开，等待包下载完毕
*   启动时自动建表
*   用户名：superadmin  密码：1
*   http://127.0.0.1/sysadmin

开发工具
======
WebStorm : Node.js
IntelliJ IDEA : Java
