<template>
    <!-- top header -->
    <header class="header header-fixed navbar">

        <div class="brand">
            <!-- toggle offscreen menu -->
            <a href="javascript:;" class="ti-menu off-left visible-xs" data-toggle="offscreen" data-move="ltr"></a>
            <!-- /toggle offscreen menu -->

            <!-- logo -->
            <a v-link="'/home'" class="navbar-brand">
                <img src="/static/images/logo.png" alt="logo">
                <span class="heading-font">NutzWK</span>
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
                <a href="/private/login/logout">
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
