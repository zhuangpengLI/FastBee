import request from '@/utils/request'

// 查询系统参数列表
export function listBaseConfig(query) {
  return request({
    url: '/iot/baseConfig/list',
    method: 'get',
    params: query
  })
}

// 查询系统参数详细
export function getBaseConfig(id) {
  return request({
    url: '/iot/baseConfig/' + id,
    method: 'get'
  })
}

// 新增系统参数
export function addBaseConfig(data) {
  return request({
    url: '/iot/baseConfig',
    method: 'post',
    data: data
  })
}

// 修改系统参数
export function updateBaseConfig(data) {
  return request({
    url: '/iot/baseConfig',
    method: 'put',
    data: data
  })
}

// 删除系统参数
export function delBaseConfig(id) {
  return request({
    url: '/iot/baseConfig/' + id,
    method: 'delete'
  })
}
