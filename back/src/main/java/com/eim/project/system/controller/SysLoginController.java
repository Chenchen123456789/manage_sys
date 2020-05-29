package com.eim.project.system.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.eim.framework.registercode.LicenseConst;
import com.eim.framework.registercode.utils.Base64Utils;
import com.eim.framework.registercode.utils.ComputerSerialNumberUtil;
import com.eim.framework.registercode.utils.FileUtil;
import com.eim.framework.registercode.utils.RSAUtils;
import com.eim.framework.security.LoginBody;
import com.eim.project.energy.service.MeasuringPointService;
import com.eim.project.system.service.impl.SysHomeSettingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.eim.common.constant.Constants;
import com.eim.common.utils.ServletUtils;
import com.eim.framework.security.LoginUser;
import com.eim.framework.security.service.SysLoginService;
import com.eim.framework.security.service.SysPermissionService;
import com.eim.framework.security.service.TokenService;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.project.system.domain.SysMenu;
import com.eim.project.system.domain.SysUser;
import com.eim.project.system.service.ISysMenuService;

/**
 * 登录验证
 *
 * @author chenchen
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysHomeSettingServiceImpl sysHomeSettingService;
    @Autowired
    private MeasuringPointService measuringPointService;

    /**
     * 登录方法
     *
     * @param loginBody 用户名
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        Map<String, String> map = checkLicense();
        ajax.put("licenseStatus", map.get("licenseStatus"));
        ajax.put("licenseMsg", map.get("licenseMsg"));
        return ajax;
    }

    public Map checkLicense() {
        Map<String, String> map = new HashMap<>();
        map.put("licenseStatus", "true");
        map.put("licenseMsg", "");
        try {
            String licenseFilePath = FileUtil.getBasePath() + File.separator + "license.dat";
//            System.out.println(licenseFilePath);
            byte[] fileEncodeData = Base64Utils.fileToByte(licenseFilePath);
//            System.out.println(new String(fileEncodeData));
            //解密
            byte[] decodedData = RSAUtils.decryptByPrivateKey(fileEncodeData, LicenseConst.privateKey);
//            System.out.println(new String(decodedData));
            JSONObject jsonObject = JSONObject.parseObject(new String(decodedData));
            if (jsonObject == null) {
                String countNumberStr = "";
                int countNumber = 0;
                if (LicenseConst.tryCountNumber == -1) {
                    countNumberStr = sysHomeSettingService.selectTempCount();
                    if ("".equals(countNumberStr) || countNumberStr == null) {
                        countNumber = 0;
                    } else {
                        try {
                            countNumber = Integer.parseInt(new String(Base64Utils.decode(countNumberStr)));
                        } catch (Exception e) {
                            countNumber = 90;
                        }
                    }
                }
                if (countNumber >= 100) {
                    map.put("licenseStatus", "false");
                    map.put("licenseMsg", "试用超出次数限制，服务将停止");
                    stopSystem(3 * 1000);
                    return map;

                } else {
                    if (LicenseConst.tryCountNumber < 0) {
                        countNumber++;
                        String decodeCountNumberStr = Base64Utils.encode(String.valueOf(countNumber).getBytes());
                        sysHomeSettingService.updateTempCount(decodeCountNumberStr);
                        LicenseConst.tryCountNumber = 1;
                    }
                    stopSystem(60 * 60 * 1000);
                    map.put("licenseStatus", "false");
                    map.put("licenseMsg", "试用模式，1小时候后服务将停止");
                    return map;
                }
            } else {
                String decodedCpuId = (String) jsonObject.getOrDefault("cpuId", "");
                String decodedDiskId = (String) jsonObject.getOrDefault("diskId", "");
                String decodedEndTime = (String) jsonObject.getOrDefault("endTime", "");
                Integer decodedSqlMaxCount = (Integer) jsonObject.getOrDefault("sqlMaxCount", 10);
                LicenseConst.sqlMaxCount = decodedSqlMaxCount;
                int currentMPCount = measuringPointService.selectMeasuringPointCount();

                Map<String, String> computerConfig = ComputerSerialNumberUtil.getAllSn();
                String cpuId = computerConfig.getOrDefault("cpuId", "");
                String diskId = computerConfig.getOrDefault("diskId", "");

                if (!(cpuId.equals(decodedCpuId) && diskId.equals(decodedDiskId) && currentMPCount <= decodedSqlMaxCount)) {
                    map.put("licenseStatus", "false");
                    map.put("licenseMsg", "注册码无效,将在1小时后停机");
                    stopSystem(60 * 60 * 1000);
                    return map;
                }
            }
        } catch (Exception e) {
            map.put("licenseStatus", "false");
            map.put("licenseMsg", e.toString());
            return map;
        }
        return map;
    }

    public void stopSystem(Integer time) {
        new Thread(() -> {
            try {
                Thread.sleep(time);
                System.out.println("1小时时间到 服务停止");
                System.exit(0);
            } catch (InterruptedException e) {
            }
        }).start();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
