<template>
  <div class="app">
    <div class="gallery-loader" style="background-color:transparent;">
      <div class="loader"></div>
    </div>
    <div class="playground hidden-xs">
      <div class="options">
        <div class="pg-close ti-close"></div>
        <div class="options-container color-options">
          <h6>样式</h6>
          <a onclick="sublimeApp.changeTheme('palette.css')" href="/static/styles/skins/palette.css"
             class="css_orange cs_color cs_1 ">
            <div></div>
            <div></div>
            <div></div>
            <div></div>
          </a>
          <a onclick="sublimeApp.changeTheme('palette.2.css')" href="/static/styles/skins/palette.2.css"
             class="css_orange cs_color cs_2 ">
            <div></div>
            <div></div>
            <div></div>
            <div></div>
          </a>
          <a onclick="sublimeApp.changeTheme('palette.3.css')" href="/static/styles/skins/palette.3.css"
             class="css_orange cs_color cs_3 ">
            <div></div>
            <div></div>
            <div></div>
            <div></div>
          </a>
        </div>
        <div class="options-container">
          <h6>布局</h6>
          <a class="pg-val toggle-sidebar toggle-active" href="javascript:;">
            <img src="/static/images/panel/small.png" alt="">
          </a>
          <a class="pg-val toggle-scroll toggle-active" href="javascript:;">
            <img src="/static/images/panel/scroll.png" alt="">
          </a>
          <a class="pg-val toggle-boxed toggle-active" href="javascript:;">
            <img src="/static/images/panel/boxed.png" alt="">
          </a>
        </div>
        <small class="pg-footer"><i class="ti-info-alt mr5"></i></small>
      </div>
    </div>
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
          url: '/platform/wx',
          icon: 'fa-wechat',
          submenus: [{
            text: '自定义菜单',
            name: 'menus',
            icon: 'fa-list',
            url: '/platform/wx/menu'
          }]
        }, {
          text: '系统管理',
          name: 'sys',
          url: '/platform/sys',
          icon: 'fa-cog',
          submenus: [{
            text: '用户管理',
            name: 'user',
            icon: 'fa-user',
            url: '/platform/sys/user'
          }, {
            text: '角色管理',
            name: 'role',
            icon: 'fa-users',
            url: '/platform/sys/role'
          }]
        }];
        this.currMenu = this.selectMenu(this.$route.path, this.allMenus);
      },
      //根据url切换菜单
      selectMenu: function (path, menus) {
        var tempMenu = {};
        console.log(path)
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
        console.log(menu.url)
        //显示loading
        $(".gallery-loader").fadeIn(200);
        this.currMenu = menu;
        this.$router.go(menu.submenus ? menu.submenus[0].url : menu.url);
        //隐藏loading
        $(".gallery-loader").fadeOut(500);
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
      //隐藏loading
      $(".gallery-loader").fadeOut(500);
    }
  }
</script>

<style>

</style>
