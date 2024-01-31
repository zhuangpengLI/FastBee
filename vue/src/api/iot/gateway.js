import request from '@/utils/request'

// 查询设备列表
export function listGateway(query) {
    return request({
        url: '/iot/gateway/list',
        method: 'get',
        params: query
    })
}


// 查询设备简短列表
export function listGatewayShort(query) {
    return request({
        url: '/iot/gateway/shortList',
        method: 'get',
        params: query
    })
}

// 删除网关
export function delDevice(query) {
    return request({
      url: '/iot/gateway/delGateway',
      method: 'delete',
      params: {
        deviceId:query
      }
    })
  }
  // 网关解绑
export function unbindingGateway(query) {
    return request({
      url: '/iot/gateway/unBindGateway',
      method: 'post',
      params: {
        deviceId:query
      }
    })
  }
    // 远程重启
export function restart(query) {
    return request({
      url: '/iot/gateway/restartGateway',
      method: 'post',
      params: {
        deviceId:query
      }
    })
  }
  // 查询上报数据列表
export function alertList(query) {
  return request({
      url: '/iot/gateway/alertList',
      method: 'get',
      params: query
  })
}
  // 查询网关升级记录列表
  export function logList(query) {
    return request({
        url: '/iot/gateway/upgradeList',
        method: 'get',
        params: query
    })
  }
  // OTA升级
  export function upgradeGateway(query) {
    return request({
        url: '/iot/gateway/upgradeGateway',
        method: 'post',
        params: query
    })
  }
    // OTA升级
    export function batchUpgradeGateway(query) {
      return request({
          url: '/iot/gateway/batchUpgradeGateway',
          method: 'post',
          params: query
      })
    }
  

  
  
