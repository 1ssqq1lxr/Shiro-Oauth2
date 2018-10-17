package com.it.uaa.common.infrastructure;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 18:29
 * @Description:
 */
public abstract class ThreadLocalHolder {

    private static ThreadLocal<String> clientIpThreadLocal = new ThreadLocal<>();

    public static void clientIp(String clientIp) {
        clientIpThreadLocal.set(clientIp);
    }

    public static String clientIp() {
        return clientIpThreadLocal.get();
    }
}