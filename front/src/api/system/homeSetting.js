import request from '@/utils/request'

// 查询菜单列表
export function getHomeSetting(query) {
  return request({
    url: '/system/homeSetting/list',
    method: 'get',
    params: query
  })
}

// 修改菜单
export function updateHomeSetting(data) {
  return request({
    url: '/system/homeSetting',
    method: 'put',
    data: data
  })
}
