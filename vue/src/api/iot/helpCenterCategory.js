import request from '@/utils/request'

// 查询帮助分类列表
export function listHelpCenterCategory(query) {
  return request({
    url: '/iot/helpCenterCategory/list',
    method: 'get',
    params: query
  })
}

// 查询帮助分类列表 不分页
export function categoryListNoPage(query) {
  return request({
    url: '/iot/helpCenterCategory/categoryListNoPage',
    method: 'get',
    params: query
  })
}


// 查询帮助分类详细
export function getHelpCenterCategory(categoryId) {
  return request({
    url: '/iot/helpCenterCategory/' + categoryId,
    method: 'get'
  })
}

// 新增帮助分类
export function addHelpCenterCategory(data) {
  return request({
    url: '/iot/helpCenterCategory',
    method: 'post',
    data: data
  })
}

// 修改帮助分类
export function updateHelpCenterCategory(data) {
  return request({
    url: '/iot/helpCenterCategory',
    method: 'put',
    data: data
  })
}

// 删除帮助分类
export function delHelpCenterCategory(categoryId) {
  return request({
    url: '/iot/helpCenterCategory/' + categoryId,
    method: 'delete'
  })
}
