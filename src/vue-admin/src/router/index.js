import Home from '@/components/modules/platform/sys/Home'
import SysUnit from '@/components/modules/platform/sys/unit/Unit'
import SysUnitAdd from '@/components/modules/platform/sys/unit/UnitAdd'

export default [
  {
    path: base+'/platform/home',
    name:'Home',
    component: Home
  },
  {
    path: base+'/platform/sys/unit',
    name:'SysUnit',
    component: SysUnit
  },
  {
    path: base+'/platform/sys/unit/add',
    name:'SysUnitAdd',
    component: SysUnitAdd
  },
  {
    path: '/',
    redirect: base+'/platform/home'
  }
]
