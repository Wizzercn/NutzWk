import Vue from 'vue'
import App from './App'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'

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
let map=Object.assign(require('./route/platform/sys').map())
map[base+'/platform/home']={component: require('./components/modules/platform/sys/Home')}

router.map(map)

router.redirect({
  '*': base+'/platform/home'
})

router.start(App, '#app')

