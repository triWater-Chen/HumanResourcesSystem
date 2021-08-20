package com.chen.myhr.common.utils.MyUtils;

import com.chen.myhr.common.utils.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * @author Chen
 * @description 图片处理工具类
 * @create 2021-08-19
 */
public class MyImage {
    private static final Logger log = LoggerFactory.getLogger(MyImage.class);

    public static byte[] getImage(String imagePath) {
        InputStream is = getFile(imagePath);
        try {
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            log.error("图片加载异常：{}", e.getMessage());
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public static InputStream getFile(String imagePath) {
        try {
            byte[] result = readFile(imagePath);
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        } catch (Exception e) {
            log.error("获取图片异常 {}", e.getMessage());
        }
        return null;
    }

    /**
     * 读取文件为字节数据
     * @param url 地址
     * @return 字节数据
     */
    public static byte[] readFile(String url) {
        InputStream in = null;
        String http = "http";
        try {
            if (url.startsWith(http)) {
                // 网络地址
                URL urlObj = new URL(url);
                URLConnection urlConnection = urlObj.openConnection();
                urlConnection.setConnectTimeout(30 * 1000);
                urlConnection.setReadTimeout(60 * 1000);
                urlConnection.setDoInput(true);
                in = urlConnection.getInputStream();
            } else {
                // 本机地址
                String localPath = CommonConstants.LOCAL_PATH;
                String downloadPath = localPath + StringUtils.substringAfter(url, "/profile");
                in = new FileInputStream(downloadPath);
            }
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            log.error("获取文件路径异常 {}", e.getMessage());
            return null;
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(null);
        }
    }
}
