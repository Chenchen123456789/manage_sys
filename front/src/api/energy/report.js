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

// 查询列表
export function listDayDosageOfWater(query) {
  return request({
    url: '/energy/report/dayDosageOfWater/list',
    method: 'post',
    data: query
  })
}

// 导出
export function exportDayDosageOfWater(query) {
  return request({
    url: '/energy/report/dayDosageOfWater/export',
    method: 'post',
    data: query
  })
}

// 查询列表
export function listQuarterDosageOfWater(query) {
  return request({
    url: '/energy/report/quarterDosageOfWater/list',
    method: 'post',
    data: query
  })
}

// 导出
export function exportQuarterDosageOfWater(query) {
  return request({
    url: '/energy/report/quarterDosageOfWater/export',
    method: 'post',
    data: query
  })
}

// 查询列表
export function listYearDosageOfWater(query) {
  return request({
    url: '/energy/report/yearDosageOfWater/list',
    method: 'post',
    data: query
  })
}

// 导出
export function exportYearDosageOfWater(query) {
  return request({
    url: '/energy/report/yearDosageOfWater/export',
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

export function listDayDosageOfElectricity(query) {
  return request({
    url: '/energy/report/dayDosageOfElectricity/list',
    method: 'post',
    data: query
  })
}

// 导出
export function exportDayDosageOfElectricity(query) {
  return request({
    url: '/energy/report/dayDosageOfElectricity/export',
    method: 'post',
    data: query
  })
}

export function listQuarterDosageOfElectricity(query) {
  return request({
    url: '/energy/report/quarterDosageOfElectricity/list',
    method: 'post',
    data: query
  })
}

// 导出
export function exportQuarterDosageOfElectricity(query) {
  return request({
    url: '/energy/report/quarterDosageOfElectricity/export',
    method: 'post',
    data: query
  })
}

export function listYearDosageOfElectricity(query) {
  return request({
    url: '/energy/report/yearDosageOfElectricity/list',
    method: 'post',
    data: query
  })
}

// 导出
export function exportYearDosageOfElectricity(query) {
  return request({
    url: '/energy/report/yearDosageOfElectricity/export',
    method: 'post',
    data: query
    
  })
}

export function listDaySettlement(query) {
  return request({
    url: '/energy/report/daySettlement/list',
    method: 'post',
    data: query
  })
}

export function exportDaySettlement(query) {
  return request({
    url: '/energy/report/daySettlement/export',
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

export function listQuarterSettlement(query) {
  return request({
    url: '/energy/report/quarterSettlement/list',
    method: 'post',
    data: query
  })
}

export function exportQuarterSettlement(query) {
  return request({
    url: '/energy/report/quarterSettlement/export',
    method: 'post',
    data: query
  })
}

export function listYearSettlement(query) {
  return request({
    url: '/energy/report/yearSettlement/list',
    method: 'post',
    data: query
  })
}

export function exportYearSettlement(query) {
  return request({
    url: '/energy/report/yearSettlement/export',
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

export function queryBuildingDosage(query={}) {
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
