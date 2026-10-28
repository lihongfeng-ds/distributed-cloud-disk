package com.util;

/**
 * 字符串工具类
 */
public class StrUtil {
    //判断是不是null或空
    public static boolean isNullOrEmpty(String s) {
        if (s == null || "".equals(s))
            return true;
        return false;
    }

    //把字节数格式成字符串
    /* 根据文件的字节数, 计算出较友好的显示形式 Byte, K, M, G , T 等
    1024 * 1024     =1M =1048576
    1024 * 1024 *1024 =1G =1073741824
    1024 * 1024 *1024 =1T =1099511627776 */
    public static String getSizeStr(float size) {
        if (size >= 1099511627776L) {
            return String.format("%.2f", size / 1024 / 1024 / 1024 / 1024) + " P";
        } else if (size >= 1073741824) {
            return String.format("%.2f", size / 1024 / 1024 / 1024) + " G";
        } else if (size >= 1048576) {
            return String.format("%.2f", size / 1024 / 1024) + " M";
        } else if (size > 1024) {
            return String.format("%.2f", size / 1024) + " K";
        } else {
            return size + " Byte";
        }
    }
}