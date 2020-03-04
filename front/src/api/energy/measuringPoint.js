import request from '@/utils/request'

// 查询仪表列表
export function listMeasuringPoint(query) {
  return request({
    url: '/energy/measuringPoint/list',
    method: 'get',
    params: query
  })
}

// 查询仪表详细
export function getMeasuringPoint(measuringPointId) {
  return request({
    url: '/energy/measuringPoint/' + measuringPointId,
    method: 'get'
  })
}

// 新增仪表
export function addMeasuringPoint(data) {
  return request({
    url: '/energy/measuringPoint',
    method: 'post',
    data: data
  })
}

// 修改仪表
export function updateMeasuringPoint(data) {
  return request({
    url: '/energy/measuringPoint',
    method: 'put',
    data: data
  })
}

// 删除仪表
export function delMeasuringPoint(measuringPointId) {
  return request({
    url: '/energy/measuringPoint/' + measuringPointId,
    method: 'delete'
  })
}

// 导出仪表
export function exportMeasuringPoint(query) {
  return request({
    url: '/energy/measuringPoint/export',
    method: 'get',
    params: query
  })
}
