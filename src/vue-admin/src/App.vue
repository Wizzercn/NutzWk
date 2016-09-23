<template>
  <div class="app" v-bind:class="currClass">
    <div class="gallery-loader" style="background-color:transparent;">
      <div class="loader"></div>
    </div>
    <div class="playground hidden-xs">
      <div class="options">
        <div class="pg-close ti-close"></div>
        <div class="options-container color-options">
          <h6>样式</h6>
          <a onclick="sublimeApp.changeTheme('palette.css')" :href="base+'/static/styles/skins/palette.css'"
             class="css_orange cs_color cs_1 "  v-bind:class="[currUser.theme=='palette.css'? 'active' : '']">
            <div></div>
            <div></div>
            <div></div>
            <div></div>
          </a>
          <a onclick="sublimeApp.changeTheme('palette.2.css')" :href="base+'/static/styles/skins/palette.2.css'"
             class="css_orange cs_color cs_2 " v-bind:class="[currUser.theme=='palette.2.css'? 'active' : '']">
            <div></div>
            <div></div>
            <div></div>
            <div></div>
          </a>
          <a onclick="sublimeApp.changeTheme('palette.3.css')" :href="base+'/static/styles/skins/palette.3.css'"
             class="css_orange cs_color cs_3 " v-bind:class="[currUser.theme=='palette.3.css'? 'active' : '']">
            <div></div>
            <div></div>
            <div></div>
            <div></div>
          </a>
        </div>
        <div class="options-container">
          <h6>布局</h6>
          <a class="pg-val toggle-sidebar toggle-active" v-bind:class="[currClass['small-menu'] ? 'active' : '']" href="javascript:;">
            <img :src="base+'/static/images/panel/small.png'" alt="">
          </a>
          <a class="pg-val toggle-scroll toggle-active" v-bind:class="[currClass['fixed-scroll'] ? 'active' : '']" href="javascript:;">
            <img :src="base+'/static/images/panel/scroll.png'" alt="">
          </a>
          <a class="pg-val toggle-boxed toggle-active" v-bind:class="[currClass['boxed'] ? 'active' : '']" href="javascript:;">
            <img :src="base+'/static/images/panel/boxed.png'" alt="">
          </a>
        </div>
        <small class="pg-footer"><i class="ti-info-alt mr5"></i></small>
      </div>
    </div>
    <navbar :curr-user="currUser" :first-menus="firstMenus" @switch-menu="switchMenu"></navbar>

    <section class="layout">
      <sidebar :curr-menu="currMenu" :custom-menus="customMenus" :path-menus="pathMenus" ></sidebar>

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
        base:base,
        firstMenus: [],
        customMenus:[],
        secondMenus: {},
        pathMenus:{},
        currMenu: {},
        subMenu: {},
        currClass:{
          'small-menu':false,
          'fixed-scroll':false,
          'boxed':false
        },
        currUser: {
          username: '',
          theme:'palette.css',
          sidebar:false
        }
      }
    },
    methods: {
      initMenus: function () {
        this.$http.get(vue+'/platform/login/userinfo').then((resp) => {
          return resp.json()
        }).then((d)=>{
          if(d.code==0){
            this.currUser.username=d.data.loginname
            this.currUser.theme=d.data.loginTheme
            this.currClass['small-menu']=d.data.loginSidebar
            this.currUser.sidebar=d.data.loginSidebar
            this.currClass['fixed-scroll']=d.data.loginScroll
            this.currClass['boxed']=d.data.loginBoxed
            this.firstMenus=d.data.firstMenus
            this.secondMenus=d.data.secondMenus
            this.pathMenus=d.data.pathMenus
            this.customMenus=d.data.customMenu
            if(this.currUser.theme){
               $("#skin").attr('href',vue+'/static/styles/skins/'+this.currUser.theme)
            }
            this.currMenu=this.selectMenu(this.$route.path, this.secondMenus, this.pathMenus);
          }else {
            toastr.error(d.msg)
          }
        })
      },
      //根据url切换菜单
      selectMenu: function (path, secondMenus,pathMenus) {
        var treeid=this.getPerPath(path)
        if(treeid){
          var menus=secondMenus[treeid.substring(0,4)]
          $.each(menus,function (i,o) {
            o[o.path]=secondMenus[o.path]
          })
          return menus
        }else return []
      },
      /**
       * 递归获取最后一级菜单树PATH
       * @param path
       * @returns {*}
       */
      getPath:function (path) {
        //如果是部署在虚拟目录,替换掉路径
        if(path.startsWith(base)){
           path=path.substring(base.length)
        }
        var p=this.pathMenus[path]
        if(p){
          return p
        }else {
          if(path&&path.lastIndexOf('/')>0){
            var s=path.substring(0,path.lastIndexOf('/'))
            return this.getPath(s)
          }else{
            return ''
          }
        }
      },
      /**
       * 获取当前路径二级菜单树PATH
       * @param path
       * @returns {*}
       */
      getPerPath:function (path) {
        //如果是部署在虚拟目录,替换掉路径
        if(path.startsWith(base)){
          path=path.substring(base.length)
        }
        var p=this.pathMenus[path]
        if(p&&p.length>=8){
          return p.substring(0,8)
        }else {
          var pp=this.getPath(path)
          if(pp){
            return pp.substring(0,8)
          }
          return ''
        }
      },
      /**
       * 切换顶部菜单
       * @param menu
       */
      switchMenu: function (menu) {
        //显示loading
        $(".gallery-loader").fadeIn(200)
        var self=this;
        var menus=this.secondMenus[menu.path]
        $.each(menus,function (i,o) {
          o[o.path]=self.secondMenus[o.path]
        })
        this.currMenu=menus
        //this.$router.go(menu.submenus ? menu.submenus[0].url : menu.url);
        //隐藏loading
        $(".gallery-loader").fadeOut(500)
      }
    },
    created: function () {
      //初始化菜单
      this.initMenus()
    },
    ready: function () {
      //初始化sublime前端框架
      window.sublimeApp = SublimeApp()
      window.sublimeApp.init();
      //隐藏loading
      $(".gallery-loader").fadeOut(500)
    }
  }
</script>

<style>

</style>
