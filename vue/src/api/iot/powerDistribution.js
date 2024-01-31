import request from '@/utils/request'

// 查询配电列表
export function gatewayDbox(query) {
  return request({
    url: '/iot/gatewayDbox/list',
    method: 'get',
    params: query
  })
}
// 查询设备列表
export function deviceList(query) {
  return request({
    url: '/iot/gatewayDbox/deviceList',
    method: 'get',
    params: query
  })
}
// 查询能耗数据
export function energyData(query) {
  return request({
    url: '/iot/gatewayDbox/energyData',
    method: 'get',
    params: query
  })
}
//查询开关数据
export function airSwitchData(query) {
  return request({
    url: '/iot/gatewayDbox/airSwitchData',
    method: 'get',
    params: query
  })
}


// 查询帮助中心内容详细
export function getHelpCenter(id) {
  return request({
    url: '/iot/helpCenter/' + id,
    method: 'get'
  })
}

// 新增帮助中心内容
export function addHelpCenter(data) {
  return request({
    url: '/iot/helpCenter',
    method: 'post',
    data: data
  })
}

// 修改帮助中心内容
export function updateHelpCenter(data) {
  return request({
    url: '/iot/helpCenter',
    method: 'put',
    data: data
  })
}

// 删除帮助中心内容
export function delHelpCenter(id) {
  return request({
    url: '/iot/helpCenter/' + id,
    method: 'delete'
  })
}
