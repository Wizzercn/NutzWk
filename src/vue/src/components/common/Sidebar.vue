<template>
  <!-- sidebar menu -->
      <aside class="sidebar offscreen-left">
        <!-- main navigation -->
        <nav class="main-navigation" data-height="auto" data-size="6px" data-distance="0" data-rail-visible="true" data-wheel-step="10">
          <p class="nav-title">菜单</p>

          <ul class="nav">
            <!-- dashboard -->
            <li class="active" v-if="currMenu.submenus==null">
              <a v-link="currMenu.url">
                <i class="fa" :class="currMenu.icon"></i>
                <span>首页</span>
              </a>
            </li>
            <!-- /dashboard -->

            <!-- ui -->
            <li :class="open" v-if="currMenu.submenus!=null">
              <a href="javascript:;">
                <i class="toggle-accordion"></i>
                <i class="fa" :class="currMenu.icon"></i>
                <span>{{currMenu.text}}</span>
              </a>
              <ul class="sub-menu" style="display:block;">
                <li v-for="sub in currMenu.submenus" :class="{'active' : sub.url==$route.path}">
                  <a v-link="sub.url">
                    <span>{{sub.text}}</span>
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
  props:['currMenu','subMenu']
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
