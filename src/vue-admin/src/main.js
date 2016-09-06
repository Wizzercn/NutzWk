import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'

import Home from './components/modules/Home'

//系统管理
import User from './components/modules/sys/User'
import Role from './components/modules/sys/Role'

//微信管理
import Welcome from './components/modules/wx/Welcome'
import Menus from './components/modules/wx/Menus'

Vue.use(VueResource)
Vue.use(VueRouter)

const router = new VueRouter({
  hashbang: true,
  history: true,//为了路径和后台统一
  saveScrollPosition: true,
  transitionOnLoad: true
})

//路由map
router.map({
    '/home': {
        component: Home
    },
    //系统管理
    '/sys/user': {
        component: User
    },
    '/sys/role': {
        component: Role
    },
    //微信管理
    '/wx/welcome': {
      component: Welcome
    },
    '/wx/menus': {
      component: Menus
    },
})

//
router.redirect({
    '*': '/home'
})

router.start(App, '#app')
