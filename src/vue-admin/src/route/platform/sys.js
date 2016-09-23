/**
 * Created by wizzer on 2016/9/22.
 */
exports.map = function () {
  return {
    //系统管理
    '/platform/sys/unit': {
      component: require('./../../components/modules/platform/sys/unit/Unit')
    },
    '/platform/sys/unit/add': {
      component: require('./../../components/modules/platform/sys/unit/UnitAdd')
    }
  }
}
