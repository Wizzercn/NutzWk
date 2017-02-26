NutzWk Vue版 前后端分离版本(Vue + Nutz)
=====
## NutzWk v3.x主分支

https://github.com/Wizzercn/NutzWk/tree/bootstrap-3.3.x   单应用快速开发, 功能与v4.x同步升级

交流群: 68428921

## 在线演示地址

https://nutzwk.wizzer.cn/                 NutzWk v3.x、v4.x

https://vue.wizzer.cn                     NutzWk v3.x-vue

## 运行环境

Vue v2.1.10 + NutzWk v3.3.5 + Webpack v2.2.1

Vue

*   node v7.6.0

NutzWk v3.x

*   JDK 8
*   Tomcat 8
*   Maven 3.3.9

开发说明
======
## 1、启动 NutzWk 后端：
*   创建空的数据库
*   修改数据库连接 /resources/config/custom/db.properties
*   项目使用Maven构建，IDEA/Eclipse直接打开，等待包下载完毕
*   启动时自动建表
*   用户名：superadmin  密码：1
*   http://127.0.0.1/sysadmin (开发vue的时候必须80端口,因为要登录才能加载菜单,除非你修改了vue的proxyTable)
*   打开登陆页会发现没有加载样式,并且登录进去什么也没有...这是正常的,因为vue通过npm run build编译成单网页应用后,再重新启动后台就可以正常登录访问了

## 2、编译 Vue 前端：

运行环境 可以build编译成单网页应用:

*   >cd src/vue-admin
*   >npm i
*   >npm run build
*   静态资源文件会打包至 src/main/webapp/aseets ,并将vue的首页文件发布至 src/main/webapp/WEB-INF/views/platform/index.html

开发环境 可以调试Vue(8080端口):

*   >npm run dev

* 访问   http://127.0.0.1:8080/

*  (若调试模块功能需通过 http://127.0.0.1/sysadmin 先登录获取身份)



开发工具
======
WebStorm : Vue

IntelliJ IDEA : Java
