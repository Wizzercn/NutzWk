import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import routes from './routes'

Vue.use(VueResource)
Vue.use(VueRouter)

const router = new VueRouter({
  routes,
  history:true,
  mode: 'hash',
  scrollBehavior: true
})

router.beforeEach((to, from, next) => {
  next()
})

const app = new Vue({
  router: router,
  render: render => render(App)
}).$mount('#app')

