import request from '@/utils/request'

// 查询建筑列表
export function testRTableList(query) {
  return request({
    url: '/energy/mqttTest/RTableList',
    method: 'get',
    params: query
  })
}

