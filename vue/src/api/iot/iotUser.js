import request from '@/utils/request'

// 查询用户列表
export function listIotUser(query) {
  return request({
    url: '/iot/user/list',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getIotUser(userId) {
  return request({
    url: '/iot/user/' + userId,
    method: 'get'
  })
}


// 删除用户
export function delIotUser(userId) {
  return request({
    url: '/iot/user/' + userId,
    method: 'delete'
  })
}
// 查询家庭列表
export function familyList(query) {
  return request({
    url: '/iot/user/familyList',
    method: 'get',
    params: query
  })
}



