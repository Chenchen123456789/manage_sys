import request from '@/utils/request'

// 查询建筑列表
export function listUnitPrice(query) {
  return request({
    url: '/energy/unitPrice/list',
    method: 'get',
    params: query
  })
}

// 修改建筑
export function updateUnitPrice(data) {
  return request({
    url: '/energy/unitPrice',
    method: 'put',
    data: data
  })
}

