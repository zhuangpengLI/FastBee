import request from '@/utils/request'

// 查询用户协议列表
export function listAgreement(query) {
  return request({
    url: '/iot/agreement/list',
    method: 'get',
    params: query
  })
}

// 查询用户协议详细
export function getAgreement(id) {
  return request({
    url: '/iot/agreement/' + id,
    method: 'get'
  })
}

// 新增用户协议
export function addAgreement(data) {
  return request({
    url: '/iot/agreement',
    method: 'post',
    data: data
  })
}

// 修改用户协议
export function updateAgreement(data) {
  return request({
    url: '/iot/agreement',
    method: 'put',
    data: data
  })
}

// 删除用户协议
export function delAgreement(id) {
  return request({
    url: '/iot/agreement/' + id,
    method: 'delete'
  })
}
