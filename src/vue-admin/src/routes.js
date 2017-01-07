/**
 * Created by wizzer on 2017/1/7.
 */
export default [
  //首页
  {
    path: base+'/platform/home',
    component: resolve => require(['./components/modules/platform/sys/Home'], resolve)
  },
  //注册
  {
    path: base+'/platform/sys/unit',
    component: resolve => require(['./components/modules/platform/sys/unit/Unit'], resolve)
  },
  //首页
  {
    path: base+'/platform/sys/unit/add',
    component: resolve => require(['./components/modules/platform/sys/unit/UnitAdd'], resolve)
  },
  {
    path: '*',
    redirect: base+'/platform/home'
  }
]
