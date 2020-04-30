package com.eim.project.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: chenchen
 * @Date: 2020/4/30
 */
public class VideoThread extends Thread {
    @Override
    public void run() {
        String command = "ffmpeg -re -i \"rtsp://www.mym9.com/101065?from=2019-06-28/01:12:13\" -c copy -f flv rtmp://127.0.0.1/live/livestream";
        String line = "";
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        Runtime.getRuntime().exec(command).getInputStream()));) {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\r\n");
            }
        } catch (IOException e) {

        }
    }
}
