package com.qianxu.mall.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/13 11:20
 * @describe
 */
public class OrderCodeFactory {
    /**
     * 订单类别头
     */
    private static final String ORDER_CODE = "1";

    /**
     * 随机编码
     */
    private static final int[] r = new int[]{7,9,6,2,8,1,3,0,5,4};

    /**
     * 用户id和随机数总长度
     */
    private static final int MAX_LENGTH = 5;

    /**
     * 根据id进行加密+加随机数组成固定长度编码
     */
    private static String toCode(Long id) {
        String idStr = id.toString();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = idStr.length() - 1; i >= 0; i--) {
            stringBuilder.append(r[idStr.charAt(i) - '0']);
        }
        return stringBuilder.append(getRandom(MAX_LENGTH - idStr.length())).toString();
    }

    /**
     * 生成时间戳
     */
    private static String getDateTime() {
        DateFormat sdf = new SimpleDateFormat("HHmmss");
        return sdf.format(new Date());
    }

    /**
     * 生成固定长度随机码
     */
    private static long getRandom(long n) {
        long min = 1, max = 9;
        for (int i = 0; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        long rangLong = ((long) (new Random().nextDouble() * (max - min))) + min;
        return rangLong;
    }

    /**
     * 生成不带类别表头的编码
     */
    private static synchronized String getCode(Long userId) {
        userId = userId == null ? 10000 : userId;
        return getDateTime() + toCode(userId);
    }

    /**
     * 生成订单单号编码
     */
    public static String getOrderCode(Long userId) {
        return ORDER_CODE + getCode(userId);
    }
}
