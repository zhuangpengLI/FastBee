import request from '@/utils/request'

// 查询系统参数列表
export function listcalendar(query) {
  return request({
    url: '/iot/sysHoliday/list',
    method: 'get',
    params: query
  })
}

// // 查询系统参数详细
// export function getBaseConfig(id) {
//   return request({
//     url: '/iot/baseConfig/' + id,
//     method: 'get'
//   })
// }

// 新增系统参数
export function addsysHoliday(data) {
  return request({
    url: '/iot/sysHoliday',
    method: 'post',
    data: data
  })
}

// 修改系统参数
export function updatesysHoliday(data) {
  return request({
    url: '/iot/sysHoliday',
    method: 'put',
    data: data
  })
}

// 删除系统参数
export function delsysHoliday(id) {
  return request({
    url: '/iot/sysHoliday/' + id,
    method: 'delete'
  })
}
