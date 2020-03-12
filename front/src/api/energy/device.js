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
export function getDevice(deviceId) {
  return request({
    url: '/energy/device/' + deviceId,
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
export function delDevice(deviceId) {
  return request({
    url: '/energy/device/' + deviceId,
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
// 下载导入模板
export function importDeviceTemplate() {
  return request({
    url: '/energy/device/importTemplate',
    method: 'get'
  })
}
