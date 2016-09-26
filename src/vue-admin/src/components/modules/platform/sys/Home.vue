<template>
    <div class="hello">
      <h1>NutzWk 4.0</h1>
      <h2>Vue + Nutz</h2>
      <ul>
        <li>add: Vue 开发环境与生产环境皆可正常运行：单网页应用部署在二级目录静态资源及路由、网路请求路径的自动替换，进度：<span style="color:green">已完成</span></li>
        <li>add: Vue 用户自定义布局和样式的入库保存和登录后加载，进度：<span style="color:green">已完成</span></li>
        <li>add: Vue 路由定义，文件加载方式，进度：<span style="color:green">已完成</span></li>
        <li>add: Vue 通过接口登录及加载用户功能菜单，进度：<span style="color:green">已完成</span></li>
        <li>add: Vue 菜单的切换及高亮显示，可通过浏览器前进、后退、刷新，进度：<span style="color:green">已完成</span></li>
        <li>add: Vue 切换顶级菜单后，默认加载最靠前的三级菜单，进度：<span style="color:red">待完成</span></li>
        <li>add: Vue 用户修改密码、常用菜单设置，进度：<span style="color:red">待完成</span></li>
        <li>add: Vue treetable组件封装，进度：<span style="color:red">待完成</span></li>
        <li>add: Vue datatables组件封装，进度：<span style="color:red">待完成</span></li>
        <li>add: Vue modal组件封装，进度：<span style="color:red">待完成</span></li>
      </ul>
      <span style="color:red">高能预警：目前只有首页和右上角切换布局可以用，其他功能开发中，点了也会报错 2233333...</span><br><br>
      NutzWk 4.x 源码地址：<a href="https://github.com/Wizzercn/NutzWk/tree/vue" target="_blank">https://github.com/Wizzercn/NutzWk/tree/vue</a><br><br>
      NutzWk 3.x 演示地址：<a href="https://nutzwk.wizzer.cn" target="_blank">https://nutzwk.wizzer.cn</a>
    </div>
</template>

<script>
    export default {
        data: function () {
            return {
                menu: {
                    name: 'home',
                    title: '首页',
                    icon: 'fa-home'
                }
            }
        },
        ready: function () {
            this.$parent.currMenu = this.menu;
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
