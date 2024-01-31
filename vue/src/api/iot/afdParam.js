import request from '@/utils/request'

// 查询语音维护列表
export function listAfdParam(query) {
  return request({
    url: '/iot/afdParam/list',
    method: 'get',
    params: query
  })
}

// 查询语音维护详细
export function getAfdParam(id) {
  return request({
    url: '/iot/afdParam/' + id,
    method: 'get'
  })
}

// 新增语音维护
export function addAfdParam(data) {
  return request({
    url: '/iot/afdParam',
    method: 'post',
    data: data
  })
}

// 修改语音维护
export function updateAfdParam(data) {
  return request({
    url: '/iot/afdParam',
    method: 'put',
    data: data
  })
}

// 删除语音维护
export function delAfdParam(id) {
  return request({
    url: '/iot/afdParam/' + id,
    method: 'delete'
  })
}
