<template>
  <!-- sidebar menu -->
      <aside class="sidebar offscreen-left">
        <!-- main navigation -->
        <nav class="main-navigation" data-height="auto" data-size="6px" data-distance="0" data-rail-visible="true" data-wheel-step="10">
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
                  <a @click="goMenu(cust)" style="cursor:pointer;">
                    <span>{{cust.name}}</span>
                  </a>
                </li>
              </ul>
            </li>
            <li v-for="menu in currMenu" :class="{'open' : menu.path==getPerPath($route.path)}" v-if="currMenu!=null&&currMenu.length>0">
              <a href="javascript:;">
                <i class="toggle-accordion"></i>
                <i class="fa" :class="menu.icon"></i>
                <span>{{menu.name}}</span>
              </a>
              <ul class="sub-menu" :style="{'display' : menu.path==getPerPath($route.path)?'block':'none'}">
                <li v-for="sub in menu[menu.path]" :class="{'active' : sub.path==getPath($route.path)}">
                  <a @click="goMenu(sub)" style="cursor:pointer;">
                    <span>{{sub.name}}</span>
                  </a>
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
  data:function(){
    return {
      base:base
    }
  },
  props:['currMenu','customMenus','pathMenus'],
  methods: {
      goMenu:function (menu) {
        $(".gallery-loader").fadeIn(200)
        this.$router.go(base+menu.href);
        $(".gallery-loader").fadeOut(300)
      },
      getPath:function (path) {
        return this.$parent.getPath(path)
      },
      getPerPath:function (path) {
        return this.$parent.getPerPath(path)
      }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
