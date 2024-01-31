import request from '@/utils/request'

// 查询配电箱空开素材列表
export function listDboxSwitch(query) {
  return request({
    url: '/iot/dboxSwitch/list',
    method: 'get',
    params: query
  })
}

// 查询配电箱空开素材详细
export function getDboxSwitch(id) {
  return request({
    url: '/iot/dboxSwitch/' + id,
    method: 'get'
  })
}

// 新增配电箱空开素材
export function addDboxSwitch(data) {
  return request({
    url: '/iot/dboxSwitch',
    method: 'post',
    data: data
  })
}

// 修改配电箱空开素材
export function updateDboxSwitch(data) {
  return request({
    url: '/iot/dboxSwitch',
    method: 'put',
    data: data
  })
}

// 删除配电箱空开素材
export function delDboxSwitch(id) {
  return request({
    url: '/iot/dboxSwitch/' + id,
    method: 'delete'
  })
}
