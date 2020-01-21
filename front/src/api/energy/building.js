import request from '@/utils/request'

// 查询建筑列表
export function listBuilding(query) {
  return request({
    url: '/energy/building/list',
    method: 'get',
    params: query
  })
}

// 查询建筑详细
export function getBuilding(buildingCode) {
  return request({
    url: '/energy/building/' + buildingCode,
    method: 'get'
  })
}

// 新增建筑
export function addBuilding(data) {
  return request({
    url: '/energy/building',
    method: 'post',
    data: data
  })
}

// 修改建筑
export function updateBuilding(data) {
  return request({
    url: '/energy/building',
    method: 'put',
    data: data
  })
}

// 删除建筑
export function delBuilding(buildingCode) {
  return request({
    url: '/energy/building/' + buildingCode,
    method: 'delete'
  })
}

// 导出建筑
export function exportBuilding(query) {
  return request({
    url: '/energy/building/export',
    method: 'get',
    params: query
  })
}
