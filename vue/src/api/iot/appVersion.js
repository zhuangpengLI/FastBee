import request from '@/utils/request'

// 查询升级管理列表
export function listAppVersion(query) {
  return request({
    url: '/iot/appVersion/list',
    method: 'get',
    params: query
  })
}

// 查询升级管理详细
export function getAppVersion(apkId) {
  return request({
    url: '/iot/appVersion/' + apkId,
    method: 'get'
  })
}

// 新增升级管理
export function addAppVersion(data) {
  return request({
    url: '/iot/appVersion',
    method: 'post',
    data: data
  })
}

// 修改升级管理
export function updateAppVersion(data) {
  return request({
    url: '/iot/appVersion',
    method: 'put',
    data: data
  })
}

// 删除升级管理
export function delAppVersion(apkId) {
  return request({
    url: '/iot/appVersion/' + apkId,
    method: 'delete'
  })
}
