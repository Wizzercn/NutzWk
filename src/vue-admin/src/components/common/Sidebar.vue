<template>
  <!-- sidebar menu -->
  <aside class="sidebar offscreen-left">
    <!-- main navigation -->
    <nav class="main-navigation" data-height="auto" data-size="6px" data-distance="0" data-rail-visible="true"
         data-wheel-step="10">
      <ul class="nav">
        <!-- ui -->
        <li class="open">
          <a href="javascript:;">
            <i class="toggle-accordion"></i>
            <i class="ti-heart"></i>
            <span>常用菜单</span>
          </a>
          <ul class="sub-menu" style="display:block;">
            <li v-for="cust in customMenus">
              <router-link :to="base + cust.href"><span>{{cust.name}}</span></router-link>
            </li>
          </ul>
        </li>
        <li v-for="menu in currMenu" :class="{'open' : menu.path==getPerPath($route.path)}"
            v-if="currMenu!=null&&currMenu.length>0">
          <a href="javascript:;">
            <i class="toggle-accordion"></i>
            <i class="fa" :class="menu.icon"></i>
            <span>{{menu.name}}</span>
          </a>
          <ul class="sub-menu" :style="{'display' : menu.path==getPerPath($route.path)?'block':'none'}">
            <li v-for="sub in menu[menu.path]" :class="{'active' : sub.path==getPath($route.path)}">
              <router-link :to="base + sub.href"><span>{{sub.name}}</span></router-link>

            </li>
          </ul>
        </li>
        <!-- /ui -->

      </ul>
    </nav>
  </aside>
  <!-- /sidebar menu -->
</template>

<script>
  export default {
    name:'Sidebar',
    data: function () {
      return {
        base: base
      }
    },
    props: ['currMenu', 'customMenus', 'pathMenus'],
    methods: {
      /**
       * 点击左侧菜单
       * @param menu
       */
      goMenu: function (menu) {
        $(".gallery-loader").fadeIn(200)
        this.$router.go(base + menu.href);
        $(".gallery-loader").fadeOut(300)
      },
      /**
       * 调用父组件方法,递归获取最后一级菜单树PATH
       * @param path
       * @returns {*}
       */
      getPath: function (path) {
        return this.$parent.getPath(path)
      },
      /**
       * 调用父组件方法,获取当前路径二级菜单树PATH
       * @param path
       * @returns {*}
       */
      getPerPath: function (path) {
        return this.$parent.getPerPath(path)
      }
    }
  }
</script>

