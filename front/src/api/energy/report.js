import request from '@/utils/request'

// 查询列表
export function listHistoryDetail(query) {
  return request({
    url: '/energy/report/historyDetail/list',
    method: 'post',
    data: query
  })
}

// 导出
export function exportHistoryDetail(query) {
  return request({
    url: '/energy/report/historyDetail/export',
    method: 'post',
    data: query
  })
}

// 查询列表
export function listMonthDosageOfWater(query) {
  return request({
    url: '/energy/report/monthDosageOfWater/list',
    method: 'post',
    data: query
  })
}

// 导出
export function exportMonthDosageOfWater(query) {
  return request({
    url: '/energy/report/monthDosageOfWater/export',
    method: 'post',
    data: query
  })
}

export function listMonthDosageOfElectricity(query) {
  return request({
    url: '/energy/report/monthDosageOfElectricity/list',
    method: 'post',
    data: query
  })
}

// 导出
export function exportMonthDosageOfElectricity(query) {
  return request({
    url: '/energy/report/monthDosageOfElectricity/export',
    method: 'post',
    data: query
  })
}

export function listMonthSettlement(query) {
  return request({
    url: '/energy/report/monthSettlement/list',
    method: 'post',
    data: query
  })
}


export function exportMonthSettlement(query) {
  return request({
    url: '/energy/report/monthSettlement/export',
    method: 'post',
    data: query
  })
}

export function queryYearTotal() {
  return request({
    url: '/energy/report/yearTotal',
    method: 'get',
  })
}

export function queryBuildingDosage(query) {
  return request({
    url: '/energy/report/buildingDosage/list',
    method: 'post',
    data: query
  })
}

export function queryBuildingDosageforHomePage() {
  return request({
    url: '/energy/report/buildingDosage/list',
    method: 'get',
  })
}

// 查询建筑分布列表
export function listBuildingDistribution() {
  return request({
    url: '/energy/report/buildingDistribution/list',
    method: 'get',
  })
}

export function listBuildingMainMPValue(buildingId = null) {
  return request({
    url: `/energy/report/buildingMainMPValue/list/${buildingId}`,
    method: 'get',
  })
}

export function listHomePageChartSetting() {
  return request({
    url: `/energy/report/homePageChartSettingList/list`,
    method: 'get',
  })
}

// 查询列表
export function listClass(query) {
  return request({
    url: '/energy/report/class/list',
    method: 'get',
    params: query
  })
}
// 查询列表
export function listEnergyType(query) {
  return request({
    url: '/energy/report/energyType/list',
    method: 'get',
    params: query
  })
}
// 查询列表
export function listSignalType(query) {
  return request({
    url: '/energy/report/signalType/list',
    method: 'get',
    params: query
  })
}

// 查询列表
export function listSystem(query) {
  return request({
    url: '/energy/report/system/list',
    method: 'get',
    params: query
  })
}
