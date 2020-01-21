import request from '@/utils/request'

// 查询工厂列表
export function listPlant(query) {
  return request({
    url: '/energy/plant/list',
    method: 'get',
    params: query
  })
}

// 查询工厂详细
export function getPlant(plantCode) {
  return request({
    url: '/energy/plant/' + plantCode,
    method: 'get'
  })
}

// 新增工厂
export function addPlant(data) {
  return request({
    url: '/energy/plant',
    method: 'post',
    data: data
  })
}

// 修改工厂
export function updatePlant(data) {
  return request({
    url: '/energy/plant',
    method: 'put',
    data: data
  })
}

// 删除工厂
export function delPlant(plantCode) {
  return request({
    url: '/energy/plant/' + plantCode,
    method: 'delete'
  })
}

// 导出工厂
export function exportPlant(query) {
  return request({
    url: '/energy/plant/export',
    method: 'get',
    params: query
  })
}
