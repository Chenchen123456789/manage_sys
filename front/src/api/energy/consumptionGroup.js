import request from '@/utils/request'

export function listConsumptionGroup(query) {
  return request({
    url: '/energy/consumptionGroup/list',
    method: 'get',
    params: query
  })
}

export function getConsumptionGroup(consumptionGroupId) {
  return request({
    url: '/energy/consumptionGroup/' + consumptionGroupId,
    method: 'get'
  })
}

export function addConsumptionGroup(data) {
  return request({
    url: '/energy/consumptionGroup',
    method: 'post',
    data: data
  })
}

export function updateConsumptionGroup(data) {
  return request({
    url: '/energy/consumptionGroup',
    method: 'put',
    data: data
  })
}

export function delConsumptionGroup(consumptionGroupId) {
  return request({
    url: '/energy/consumptionGroup/' + consumptionGroupId,
    method: 'delete'
  })
}


export function exportConsumptionGroup(query) {
  return request({
    url: '/energy/consumptionGroup/export',
    method: 'get',
    params: query
  })
}

// 下载导入模板
export function importConsumptionGroupTemplate() {
  return request({
    url: '/energy/consumptionGroup/importTemplate',
    method: 'get'
  })
}
