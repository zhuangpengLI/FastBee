import request from '@/utils/request'

// 查询家庭管理列表
export function listFamily(query) {
  return request({
    url: '/iot/family/list',
    method: 'get',
    params: query
  })
}
// 查询房间列表
export function listRoom(query) {
  return request({
    url: '/iot/family/roomList',
    method: 'get',
    params: query
  })
}
// 查询未分配设备数量
export function countNotInRomm(query) {
  return request({
    url: '/iot/family/countNotInRomm',
    method: 'get',
    params: query
  })
}
// 查询成员
export function userList(query) {
  return request({
    url: '/iot/family/userList',
    method: 'get',
    params: query
  })
}

// 查询家庭管理详细
export function getFamily(familyId) {
  return request({
    url: '/iot/family/' + familyId,
    method: 'get'
  })
}

// 新增家庭管理
export function addFamily(data) {
  return request({
    url: '/iot/family',
    method: 'post',
    data: data
  })
}

// 修改家庭管理
export function updateFamily(data) {
  return request({
    url: '/iot/family',
    method: 'put',
    data: data
  })
}

// 删除家庭管理
export function delFamily(familyId) {
  return request({
    url: '/iot/family/' + familyId,
    method: 'delete'
  })
}
