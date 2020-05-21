package com.eim.framework.task;

import com.eim.common.utils.ServletUtils;
import com.eim.framework.security.LoginUser;
import com.eim.framework.security.service.TokenService;
import com.eim.project.energy.entity.AlarmLog;
import com.eim.project.energy.service.AlarmLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.eim.common.utils.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时任务调度测试
 *
 * @author chenchen
 */
@Component("ryTask")
public class RyTask {
    @Autowired
    private AlarmLogService alarmLogService;

    @Autowired
    private TokenService tokenService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }

    public void genAlarmLog() {
        List<Map<String, Object>> list = alarmLogService.selectAlarmTagValueList();
        for (Map<String, Object> map : list) {
            String tagName = (String) map.get("tagName");
            Double tagValue = Double.valueOf((String) map.get("tagValue"));
            Integer alarmTag = (Integer) map.getOrDefault("alarmTag", 0);
            Double alarmLLValue = (Double) map.getOrDefault("alarmLLValue", 0.0);
            Double alarmLValue = (Double) map.getOrDefault("alarmLValue", 0.0);
            Double alarmHValue = (Double) map.getOrDefault("alarmHValue", 0.0);
            Double alarmHHValue = (Double) map.getOrDefault("alarmHHValue", 0.0);
            Integer alarmDigitalValue = (Integer) map.getOrDefault("alarmDigitalValue", 0);
            Integer alarmLevel = (Integer) map.getOrDefault("alarmLevel", 1);
            String description = (String) map.getOrDefault("description","");
            String alarmDigitalZeroDescription = (String) map.getOrDefault("alarmDigitalZeroDescription","");
            String alarmDigitalOneDescription = (String) map.getOrDefault("alarmDigitalOneDescription","");

            String alarmLevelStr = "";
            switch (alarmLevel) {
                case 1:
                    alarmLevelStr = "普通";
                    break;
                case 2:
                    alarmLevelStr = "一般";
                    break;
                case 3:
                    alarmLevelStr = "紧急";
                    break;
                case 4:
                    alarmLevelStr = "特急";
                    break;
                default:
                    break;
            }
            AlarmLog existAlarmLog = alarmLogService.selectAlarmLogByTagName(tagName);
            String existStatus = "";
            String bgColor = "";
            if (existAlarmLog != null) {
                existStatus = existAlarmLog.getStatus();
                bgColor = existAlarmLog.getBgColor();
            }
            String status = "";

            //模拟量
            if (alarmTag == 1) {
                if (alarmLLValue == 0 && alarmLValue == 0 && alarmHValue == 0 && alarmHHValue == 0) {
                    continue;
                }
                //非正常值
                if (tagValue <= alarmLLValue) {
                    status = "LL";
                    AlarmLog alarmLog = new AlarmLog();
                    alarmLog.setDescription(description)
                            .setTagName(tagName)
                            .setTagValue(tagValue)
                            .setAlarmTime(new Date())
                            .setStatus(status)
                            .setLevel(alarmLevelStr)
                            .setBgColor("yellow");
                    if (existAlarmLog == null) {
                        alarmLogService.insertAlarmLog(alarmLog);
                    } else {
                        if (bgColor.equals("purple") || bgColor.equals("green")) {
                            alarmLogService.insertAlarmLog(alarmLog);
                        } else {
                            if (!status.equals(existStatus)) {
                                alarmLogService.insertAlarmLog(alarmLog);
                            }
                        }
                    }
                } else if (tagValue <= alarmLValue && tagValue > alarmLLValue) {
                    status = "L";
                    AlarmLog alarmLog = new AlarmLog();
                    alarmLog.setDescription(description)
                            .setTagName(tagName)
                            .setTagValue(tagValue)
                            .setAlarmTime(new Date())
                            .setStatus(status)
                            .setLevel(alarmLevelStr)
                            .setBgColor("yellow");
                    if (existAlarmLog == null) {
                        alarmLogService.insertAlarmLog(alarmLog);
                    } else {
                        if (bgColor.equals("purple") || bgColor.equals("green")) {
                            alarmLogService.insertAlarmLog(alarmLog);
                        } else {
                            if (!status.equals(existStatus)) {
                                alarmLogService.insertAlarmLog(alarmLog);
                            }
                        }
                    }
                } else if (tagValue < alarmHHValue && tagValue >= alarmHValue) {
                    status = "H";
                    AlarmLog alarmLog = new AlarmLog();
                    alarmLog.setDescription(description)
                            .setTagName(tagName)
                            .setTagValue(tagValue)
                            .setAlarmTime(new Date())
                            .setStatus(status)
                            .setLevel(alarmLevelStr)
                            .setBgColor("red");
                    if (existAlarmLog == null) {
                        alarmLogService.insertAlarmLog(alarmLog);
                    } else {
                        if (bgColor.equals("purple") || bgColor.equals("green")) {
                            alarmLogService.insertAlarmLog(alarmLog);
                        } else {
                            if (!status.equals(existStatus)) {
                                alarmLogService.insertAlarmLog(alarmLog);
                            }
                        }
                    }
                } else if (tagValue >= alarmHHValue) {
                    status = "HH";
                    AlarmLog alarmLog = new AlarmLog();
                    alarmLog.setDescription(description)
                            .setTagName(tagName)
                            .setTagValue(tagValue)
                            .setAlarmTime(new Date())
                            .setStatus(status)
                            .setLevel(alarmLevelStr)
                            .setBgColor("red");
                    if (existAlarmLog == null) {
                        alarmLogService.insertAlarmLog(alarmLog);
                    } else {
                        if (bgColor.equals("purple") || bgColor.equals("green")) {
                            alarmLogService.insertAlarmLog(alarmLog);
                        } else {
                            if (!status.equals(existStatus)) {
                                alarmLogService.insertAlarmLog(alarmLog);
                            }
                        }
                    }
                }
                //正常值
                else {
                    if (existAlarmLog != null) {
                        if (!existStatus.equals("green") && !existStatus.equals("purple")) {
                            alarmLogService.updateCurrentAlarmLogList(tagName);
                        }
                    }
                }
                //开关量
            } else if (alarmTag == 2) {
                if (alarmDigitalValue == 0) {
                    //非正常值
                    if (tagValue == 0) {
//                        status = "0";
                        AlarmLog alarmLog = new AlarmLog();
                        alarmLog.setDescription(description)
                                .setTagName(tagName)
                                .setTagValue(tagValue)
                                .setAlarmTime(new Date())
                                .setStatus(alarmDigitalZeroDescription)
                                .setLevel(alarmLevelStr)
                                .setBgColor("yellow");
                        if (existAlarmLog == null) {
                            alarmLogService.insertAlarmLog(alarmLog);
                        } else {
                            if (bgColor.equals("purple") || bgColor.equals("green")) {
                                alarmLogService.insertAlarmLog(alarmLog);
                            } else {
                                if (!status.equals(existStatus)) {
                                    alarmLogService.insertAlarmLog(alarmLog);
                                }
                            }
                        }
                    }//正常值
                    else if (tagValue == 1) {
                        if (existAlarmLog != null) {
                            if (!existStatus.equals("green") && !existStatus.equals("purple")) {
                                alarmLogService.updateCurrentAlarmLogList(tagName);
                            }
                        }
                    }
                } else if (alarmDigitalValue == 1) {
                    //非正常值
                    if (tagValue == 1) {
//                        status = "1";
                        AlarmLog alarmLog = new AlarmLog();
                        alarmLog.setDescription(description)
                                .setTagName(tagName)
                                .setTagValue(tagValue)
                                .setAlarmTime(new Date())
                                .setStatus(alarmDigitalOneDescription)
                                .setLevel(alarmLevelStr)
                                .setBgColor("yellow");
                        if (existAlarmLog == null) {
                            alarmLogService.insertAlarmLog(alarmLog);
                        } else {
                            if (bgColor.equals("purple") || bgColor.equals("green")) {
                                alarmLogService.insertAlarmLog(alarmLog);
                            } else {
                                if (!status.equals(existStatus)) {
                                    alarmLogService.insertAlarmLog(alarmLog);
                                }
                            }
                        }
                    }//正常值
                    else if (tagValue == 0) {
                        if (existAlarmLog != null) {
                            if (!existStatus.equals("green") && !existStatus.equals("purple")) {
                                alarmLogService.updateCurrentAlarmLogList(tagName);
                            }
                        }
                    }
                } else if (alarmDigitalValue == 2) {
                    //非正常值
                    if (tagValue == 1 || tagValue == 0) {
                        String color = "";
                        if (tagValue == 1) {
                            color = "red";
                            status = alarmDigitalZeroDescription;
                        } else if (tagValue == 0) {
                            color = "yellow";
                            status = alarmDigitalOneDescription;
                        }
                        AlarmLog alarmLog = new AlarmLog();
                        alarmLog.setDescription(description)
                                .setTagName(tagName)
                                .setTagValue(tagValue)
                                .setAlarmTime(new Date())
                                .setStatus(status)
                                .setLevel(alarmLevelStr)
                                .setBgColor(color);
                        if (existAlarmLog == null) {
                            alarmLogService.insertAlarmLog(alarmLog);
                        } else {
                            if (bgColor.equals("purple") || bgColor.equals("green")) {
                                alarmLogService.insertAlarmLog(alarmLog);
                            } else {
                                if (!status.equals(existStatus)) {
                                    alarmLogService.insertAlarmLog(alarmLog);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("执行genAlarmLog");
    }
}
