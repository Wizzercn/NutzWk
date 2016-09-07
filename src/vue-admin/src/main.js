import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'

import Home from './components/modules/Home'

//系统管理
import User from './components/modules/platform/sys/User'
import Role from './components/modules/platform/sys/Role'

//微信管理
import Welcome from './components/modules/platform/wx/Welcome'
import Menus from './components/modules/platform/wx/Menus'

Vue.use(VueResource)
Vue.use(VueRouter)

const router = new VueRouter({
  history:true,
  saveScrollPosition:true
})

//路由map
router.map({
    '/home': {
        component: Home
    },
    //系统管理
    '/platform/sys/user': {
        component: User
    },
    '/platform/sys/role': {
        component: Role
    },
    //微信管理
    '/platform/wx/welcome': {
      component: Welcome
    },
    '/platform/wx/menus': {
      component: Menus
    },
})

//
router.redirect({
    '*': '/home'
})

router.start(App, '#app')
