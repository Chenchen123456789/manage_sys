import request from '@/utils/request'

// 查询设备列表
export function listDevice(query) {
  return request({
    url: '/energy/device/list',
    method: 'get',
    params: query
  })
}

// 查询设备详细
export function getDevice(deviceCode) {
  return request({
    url: '/energy/device/' + deviceCode,
    method: 'get'
  })
}

// 新增设备
export function addDevice(data) {
  return request({
    url: '/energy/device',
    method: 'post',
    data: data
  })
}

// 修改设备
export function updateDevice(data) {
  return request({
    url: '/energy/device',
    method: 'put',
    data: data
  })
}

// 删除设备
export function delDevice(deviceCode) {
  return request({
    url: '/energy/device/' + deviceCode,
    method: 'delete'
  })
}

// 导出设备
export function exportDevice(query) {
  return request({
    url: '/energy/device/export',
    method: 'get',
    params: query
  })
}
