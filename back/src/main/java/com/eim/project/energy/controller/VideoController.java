package com.eim.project.energy.controller;

import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.project.tool.VideoThread;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: chenchen
 * @Date: 2020/4/30
 */
@RestController
@RequestMapping("/energy/video")
public class VideoController extends BaseController {
    //运行cmd

    @PostMapping("/preview")
    public AjaxResult preview() {
        VideoThread videoThread = new VideoThread();
        videoThread.start();
        return AjaxResult.success("");
    }

    //关闭cmd窗口
    public static void closeCmd() {
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            rt.exec("cmd.exe /C start wmic process where name='cmd.exe' call terminate");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
