import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import Routers from './router'

Vue.use(VueResource)
Vue.use(VueRouter)

const router = new VueRouter({
  routes:Routers,
  mode: 'history'
})

router.beforeEach((to, from, next) => {
  next()
})

const app = new Vue({
  router: router,
  render: render => render(App)
}).$mount('#app')
