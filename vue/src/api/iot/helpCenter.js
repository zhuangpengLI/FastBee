import request from '@/utils/request'

// 查询帮助中心内容列表
export function listHelpCenter(query) {
  return request({
    url: '/iot/helpCenter/list',
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
