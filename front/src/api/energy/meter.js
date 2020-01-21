import request from '@/utils/request'

// 查询仪表列表
export function listMeter(query) {
  return request({
    url: '/energy/meter/list',
    method: 'get',
    params: query
  })
}

// 查询仪表详细
export function getMeter(meterCode) {
  return request({
    url: '/energy/meter/' + meterCode,
    method: 'get'
  })
}

// 新增仪表
export function addMeter(data) {
  return request({
    url: '/energy/meter',
    method: 'post',
    data: data
  })
}

// 修改仪表
export function updateMeter(data) {
  return request({
    url: '/energy/meter',
    method: 'put',
    data: data
  })
}

// 删除仪表
export function delMeter(meterCode) {
  return request({
    url: '/energy/meter/' + meterCode,
    method: 'delete'
  })
}

// 导出仪表
export function exportMeter(query) {
  return request({
    url: '/energy/meter/export',
    method: 'get',
    params: query
  })
}
