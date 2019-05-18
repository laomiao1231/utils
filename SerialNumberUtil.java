package com.m.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SerialNumberUtil {
    /**
     * 生成随机序列号
     * @return
     */
    public synchronized static String getSerialNumber() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String currentDataValue = format.format(new Date());
        int randomSerialNumber = new Random().nextInt(10000);
        StringBuffer serialNumber = new StringBuffer();
        serialNumber.append(currentDataValue).append(System.currentTimeMillis()).append(randomSerialNumber);
        return serialNumber.toString();
    }
}
