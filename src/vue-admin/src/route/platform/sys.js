/**
 * Created by wizzer on 2016/9/22.
 */
let map={}
map[base+'/platform/sys/unit']={
  component: require('./../../components/modules/platform/sys/unit/Unit')
}
map[base+'/platform/sys/unit/add']={
  component: require('./../../components/modules/platform/sys/unit/UnitAdd')
}
exports.map = function () {
  return map
}
