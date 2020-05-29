package com.eim.framework.registercode.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 文件工具类
 *
 * @author chenchen
 */
public class FileUtil {

    /**
     * 获得类的基路径，打成jar包也可以正确获得路径
     *
     * @return
     */
    public static String getBasePath() {
        /*
        /D:/zhao/Documents/NetBeansProjects/docCompare/build/classes/
        /D:/zhao/Documents/NetBeansProjects/docCompare/dist/bundles/docCompare/app/docCompare.jar
        */
        String filePath = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();


        if (filePath.contains(".jar")) {
            filePath = filePath.substring(0, filePath.indexOf("file:"));
            try {
                //解决路径中有空格%20的问题
                filePath = URLDecoder.decode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException ex) {

            }

        }
        File file = new File(filePath);
        filePath = file.getAbsolutePath();
        return filePath;
    }
}
