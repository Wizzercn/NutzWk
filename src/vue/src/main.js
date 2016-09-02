import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'

import Home from './components/modules/Home'

//系统管理
import User from './components/modules/sys/User'
import Role from './components/modules/sys/Role'

//微信管理
import Welcome from './components/modules/weixin/Welcome'
import Menus from './components/modules/weixin/Menus'

Vue.use(VueResource)
Vue.use(VueRouter)

const router = new VueRouter()

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
    '/weixin/welcome': {
      component: Welcome
    },
    '/weixin/menus': {
      component: Menus
    },
})

//
router.redirect({
    '*': '/home'
})

router.start(App, '#app')
