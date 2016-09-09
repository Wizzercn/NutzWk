<template>
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
    <!-- top header -->
    <header class="header header-fixed navbar">

        <div class="brand">
            <!-- toggle offscreen menu -->
            <a href="javascript:;" class="ti-menu off-left visible-xs" data-toggle="offscreen" data-move="ltr"></a>
            <!-- /toggle offscreen menu -->

            <!-- logo -->
            <a v-link="'/home'" class="navbar-brand">
                <img src="/static/images/logo.png" alt="logo">
                <span class="heading-font">NutzWk</span>
            </a>
            <!-- /logo -->
        </div>

        <ul class="nav navbar-nav">
            <li class="hidden-xs">
                <!-- toggle small menu -->
                <a href="javascript:;" class="toggle-sidebar">
                    <i class="ti-menu"></i>
                </a>
                <!-- /toggle small menu -->
            </li>
        </ul>

        <ul id="topnav" class="nav navbar-nav hidden-xs">

            <li v-for="menu in allMenus">
                <a href="javascript:;" @click="switchMenu(menu)">
                    <i class="fa" :class="menu.icon"></i>
                    <span>{{menu.text}}</span>
                </a>
            </li>
        </ul>

        <ul class="nav navbar-nav navbar-right">
            <!--通知位置-->
            <li class="language-dropdown dropdown hidden-xs">
                <a href="javascript:;" data-toggle="dropdown" id="language">
                    <img src="/static/images/avatar.jpg"  class="flag">
                    <ul class="dropdown-menu dropdown-menu-right animated fadeInUp">
                        <li>
                            <a href="?lang=en_US">
                                <img src="/static/images/avatar.jpg" class="flag">
                                <span class="language">English</span>
                            </a>
                        </li>
                        <li>
                            <a href="?lang=zh_CN">
                                <img class="flag">
                                <span class="language">中文</span>
                            </a>
                        </li>
                    </ul>
                </a>
            </li>
            <li class="off-right">
                <a href="javascript:;" data-toggle="dropdown">
                    <span class="hidden-xs ml10">{{currUser.username}}</span>
                    <i class="ti-angle-down ti-caret hidden-xs"></i>
                </a>
                <ul class="dropdown-menu animated fadeInLeft">
                    <li>
                        <a href="" data-toggle="modal"
                           data-target="#homeDetail">常用菜单</a>
                    </li>

                    <li class="dropdown hidden-xs">
                        <a href="javascript:;" class="pg-toggle">
                            页面布局
                        </a>
                    </li>
                    <li>
                        <a href="" data-toggle="modal"
                           data-target="#homeDetail">修改密码</a>
                    </li>
                </ul>
            </li>
            <li class="off-right">
                <a href="/platform/login/logout">
                    <i class="ti-power-off"></i>
                </a>
            </li>
        </ul>

    </header>
    <!-- /top header -->
</template>

<script>
    export default {
        data: function() {
            return {

            }
        },
        props: ['currUser','allMenus'],
        methods: {
            switchMenu: function(menu) {
                this.$emit('switch-menu', menu);
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
