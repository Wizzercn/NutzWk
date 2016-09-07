<template>
  <div class="app">
    <navbar :curr-user="currUser" :all-menus="allMenus" @switch-menu="switchMenu"></navbar>

    <section class="layout">
      <sidebar :curr-menu="currMenu" :sub-menu="subMenu"></sidebar>

      <!-- main content -->
      <section class="main-content">

        <!-- content wrapper -->
        <div class="content-wrap">
          <!-- inner content wrapper -->
          <div class="wrapper no-p">
            <router-view></router-view>
          </div>
          <!-- /inner content wrapper -->

        </div>
        <!-- /content wrapper -->
        <a class="exit-offscreen"></a>
      </section>
      <!-- /main content -->

    </section>
  </div>
</template>

<script>
  import Navbar from './components/common/Navbar'
  import Sidebar from './components/common/Sidebar'

  export default {
    components: {
      Navbar,
      Sidebar
    },
    data: function () {
      return {
        allMenus: [],
        currMenu: {},
        subMenu: {},
        currUser: {
          username: 'superadmin',
          avatar: 'static/images/avatar.jpg'
        }
      }
    },
    methods: {
      initMenus: function () {
        this.allMenus = [{
          text: '微信管理',
          name: 'weixin',
          url: '/weixin',
          icon: 'fa-wechat',
          submenus: [{
            text: '欢迎语',
            name: 'welcome',
            icon: 'fa-msg',
            url: '/weixin/welcome'
          }, {
            text: '自定义菜单',
            name: 'menus',
            icon: 'fa-list',
            url: '/weixin/menus'
          }]
        }, {
          text: '系统管理',
          name: 'sys',
          url: '/sys',
          icon: 'fa-cog',
          submenus: [{
            text: '用户管理',
            name: 'user',
            icon: 'fa-user',
            url: '/sys/user'
          }, {
            text: '角色管理',
            name: 'role',
            icon: 'fa-users',
            url: '/sys/role'
          }]
        }];
        this.currMenu = this.selectMenu(this.$route.path, this.allMenus);
      },
      //根据url切换菜单
      selectMenu: function (path, menus) {
        var tempMenu = {};

        //父菜单判断
        $.each(menus, function (index, menu) {
          if (menu.url == path) {
            tempMenu = menu;
          }

          //子菜单判断
          var submenus = menu.submenus;
          if (path.lastIndexOf('/') > 0 && submenus != null) {
            $.each(submenus, function (index, sub) {
              if (sub.url == path) {
                tempMenu = menu;
              }
            });
          }

        });
        return tempMenu;
      },
      //切换菜单
      switchMenu: function (menu) {
        this.currMenu = menu;
        this.$router.go(menu.submenus ? menu.submenus[0].url : menu.url);
      }
    },
    created: function () {
      //初始化菜单
      this.initMenus();
    },
    ready: function () {
      //初始化sublime前端框架
      window.sublimeApp = SublimeApp();
      window.sublimeApp.init();
    }
  }
</script>

<style>

</style>
