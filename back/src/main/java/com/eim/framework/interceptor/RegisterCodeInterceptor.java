//package com.eim.framework.interceptor;
//
//import com.alibaba.fastjson.JSONObject;
//import com.eim.common.utils.ServletUtils;
//import com.eim.framework.registercode.LicenseConst;
//import com.eim.framework.registercode.utils.Base64Utils;
//import com.eim.framework.registercode.utils.ComputerSerialNumberUtil;
//import com.eim.framework.registercode.utils.FileUtil;
//import com.eim.framework.registercode.utils.RSAUtils;
//import com.eim.framework.web.domain.AjaxResult;
//import com.eim.project.energy.service.MeasuringPointService;
//import com.eim.project.energy.service.UnitPriceService;
//import com.eim.project.system.service.impl.SysHomeSettingServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.util.Map;
//
///**
// * @Author: chenchen
// * @Date: 2020/5/26
// */
//@Component
//public class RegisterCodeInterceptor extends HandlerInterceptorAdapter {
//    @Autowired
//    private SysHomeSettingServiceImpl sysHomeSettingService;
//    @Autowired
//    private MeasuringPointService measuringPointService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//
//        try {
//            String licenseFilePath = FileUtil.getBasePath() + File.separator + "license.dat";
//
//            byte[] fileEncodeData = Base64Utils.fileToByte(licenseFilePath);
//            System.out.println(fileEncodeData);
//            //解密
//            byte[] decodedData = RSAUtils.decryptByPrivateKey(fileEncodeData, LicenseConst.privateKey);
//            JSONObject jsonObject = JSONObject.parseObject(new String(decodedData));
//            if (jsonObject == null) {
//                int countNumber = 0;
//                if (LicenseConst.tryCountNumber == -1) {
//                    countNumber = sysHomeSettingService.selectTempCount();
////                    LicenseConst.tryCountNumber = countNumber;
//                }
//                if (countNumber >= 100) {
//                    AjaxResult ajaxResult = AjaxResult.error("试用超出次数限制");
//                    ServletUtils.renderString(response, JSONObject.toJSONString(ajaxResult));
//                    return false;
//                } else {
//                    if (LicenseConst.tryCountNumber < 0) {
//                        countNumber++;
//                        sysHomeSettingService.updateTempCount(countNumber);
//                        LicenseConst.tryCountNumber = 1;
//                    }
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(60 * 60 * 1000);
//                                System.exit(0);
//                            } catch (InterruptedException e) {
//                            }
//                        }
//                    }.start();
//                }
//            } else {
//                String decodedCpuId = (String) jsonObject.getOrDefault("cpuId", "");
//                String decodedDiskId = (String) jsonObject.getOrDefault("diskId", "");
//                String decodedEndTime = (String) jsonObject.getOrDefault("endTime", "");
//                Integer decodedSqlMaxCount = (Integer) jsonObject.getOrDefault("sqlMaxCount", 10);
//                LicenseConst.sqlMaxCount = decodedSqlMaxCount;
//                int currentMPCount = measuringPointService.selectMeasuringPointCount();
//
//                Map<String, String> computerConfig = ComputerSerialNumberUtil.getAllSn();
//                String cpuId = computerConfig.getOrDefault("cpuId", "");
//                String diskId = computerConfig.getOrDefault("diskId", "");
//
//                if (!(cpuId.equals(decodedCpuId) && diskId.equals(decodedDiskId) && currentMPCount <= decodedSqlMaxCount)) {
//                    AjaxResult ajaxResult = AjaxResult.success("注册码无效,将在1小时候停机");
//                    ServletUtils.renderString(response, JSONObject.toJSONString(ajaxResult));
//                }
//            }
//        } catch (Exception e) {
//            AjaxResult ajaxResult = AjaxResult.error(e.toString());
//            ServletUtils.renderString(response, JSONObject.toJSONString(ajaxResult));
//            return false;
//        }
//        return true;
//    }
//}
