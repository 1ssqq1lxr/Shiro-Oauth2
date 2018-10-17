package com.it.uaa.common.domain.shared;

import java.util.UUID;

/**
 * @Auther: lxr
 * @Date: 2018/9/28 18:29
 * @Description:
 */
public abstract class GuidGenerator {

    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}