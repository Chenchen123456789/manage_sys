import request from '@/utils/request'

export function listMeasuringPoint() {
  return request({
    url: '/energy/alarmLog/measuringPoint/list',
    method: 'get'
  })
}

export function getMeasuringPoint(tagName) {
  return request({
    url: '/energy/alarmLog/measuringPoint',
    method: 'post',
    data: {
      tagName
    }
  })
}

export function listAlarmLog(query) {
  return request({
    url: '/energy/alarmLog/list',
    method: 'post',
    data: query
  })
}

export function getAlarmLog(alarmLogId) {
  return request({
    url: '/energy/alarmLog/' + alarmLogId,
    method: 'get'
  })
}

export function getUnAckAlarmLog() {
  return request({
    url: '/energy/alarmLog/getUnAckAlarmLog',
    method: 'get'
  })
}

export function updateAlarmLog(ids) {
  return request({
    url: '/energy/alarmLog/' + ids,
    method: 'put',
  })
}

export function exportAlarmLog(query) {
  return request({
    url: '/energy/alarmLog/export',
    method: 'post',
    data: query
  })
}
