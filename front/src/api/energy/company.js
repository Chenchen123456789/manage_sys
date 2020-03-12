import request from '@/utils/request'

// 查询公司列表
export function listCompany(query) {
  return request({
    url: '/energy/company/list',
    method: 'get',
    params: query
  })
}

// 查询公司详细
export function getCompany(companyId) {
  return request({
    url: '/energy/company/' + companyId,
    method: 'get'
  })
}

// 新增公司
export function addCompany(data) {
  return request({
    url: '/energy/company',
    method: 'post',
    data: data
  })
}

// 修改公司
export function updateCompany(data) {
  return request({
    url: '/energy/company',
    method: 'put',
    data: data
  })
}

// 删除公司
export function delCompany(companyId) {
  return request({
    url: '/energy/company/' + companyId,
    method: 'delete'
  })
}

// 导出公司
export function exportCompany(query) {
  return request({
    url: '/energy/company/export',
    method: 'get',
    params: query
  })
}

// 下载导入模板
export function importCompanyTemplate() {
  return request({
    url: '/energy/company/importTemplate',
    method: 'get'
  })
}
