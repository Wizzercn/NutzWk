import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'

//系统管理
import SysHome from './components/modules/platform/sys/SysHome'
import SysUser from './components/modules/platform/sys/user/SysUser'
import SysUserAdd from './components/modules/platform/sys/user/SysUserAdd'
import SysRole from './components/modules/platform/sys/SysRole'

//微信管理
import WxMenu from './components/modules/platform/wx/WxMenu'

Vue.use(VueResource)
Vue.use(VueRouter)
const router = new VueRouter({
  history:true,
  saveScrollPosition:true
})

router.beforeEach((transition) => {
  transition.next()
})

//路由map
router.map({
    '/platform/home': {
        component: SysHome
    },
    //系统管理
    '/platform/sys/user': {
      component: SysUser
    },
    '/platform/sys/user/add': {
      component: SysUserAdd
    },
    '/platform/sys/role': {
        component: SysRole
    },
    //微信管理
    '/platform/wx/menu': {
        component: WxMenu
    },
})

router.redirect({
  '*': '/platform/home'
})

router.start(App, '#app')

