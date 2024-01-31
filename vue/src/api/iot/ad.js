import request from '@/utils/request'

// 查询广告列表
export function listAd(query) {
  return request({
    url: '/iot/ad/list',
    method: 'get',
    params: query
  })
}

// 查询广告详细
export function getAd(id) {
  return request({
    url: '/iot/ad/' + id,
    method: 'get'
  })
}

// 新增广告
export function addAd(data) {
  return request({
    url: '/iot/ad',
    method: 'post',
    data: data
  })
}

// 修改广告
export function updateAd(data) {
  return request({
    url: '/iot/ad',
    method: 'put',
    data: data
  })
}

// 删除广告
export function delAd(id) {
  return request({
    url: '/iot/ad/' + id,
    method: 'delete'
  })
}
