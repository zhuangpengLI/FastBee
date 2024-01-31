import request from '@/utils/request'

// 查询配电箱配置列表
export function listDbox(query) {
  return request({
    url: '/iot/dbox/list',
    method: 'get',
    params: query
  })
}

// 查询配电箱配置详细
export function getDbox(id) {
  return request({
    url: '/iot/dbox/' + id,
    method: 'get'
  })
}

// 新增配电箱配置
export function addDbox(data) {
  return request({
    url: '/iot/dbox',
    method: 'post',
    data: data
  })
}

// 修改配电箱配置
export function updateDbox(data) {
  return request({
    url: '/iot/dbox',
    method: 'put',
    data: data
  })
}

// 删除配电箱配置
export function delDbox(id) {
  return request({
    url: '/iot/dbox/' + id,
    method: 'delete'
  })
}
