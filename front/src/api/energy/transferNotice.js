import request from '@/utils/request'

export function listTransferNotice(query) {
  return request({
    url: '/energy/transferNotice/list',
    method: 'post',
    data: query
  })
}

export function getTransferNotice(transferNoticeId) {
  return request({
    url: '/energy/transferNotice/' + transferNoticeId,
    method: 'get'
  })
}

export function addTransferNotice(data) {
  return request({
    url: '/energy/transferNotice',
    method: 'post',
    data: data
  })
}

export function updateTransferNotice(data) {
  return request({
    url: '/energy/transferNotice',
    method: 'put',
    data: data
  })
}

export function delTransferNotice(transferNoticeId) {
  return request({
    url: '/energy/transferNotice/' + transferNoticeId,
    method: 'delete'
  })
}

export function exportTransferNotice(query) {
  return request({
    url: '/energy/transferNotice/export',
    method: 'post',
    data: query
  })
}
