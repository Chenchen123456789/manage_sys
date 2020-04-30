import request from '@/utils/request'

// 查询建筑列表
export function videoPreview(data = {}) {
  return request({
    url: '/energy/video/preview',
    method: 'post',
    data
  })
}
