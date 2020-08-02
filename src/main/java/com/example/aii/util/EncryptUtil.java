package com.example.aii.util;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.UUID;

public class EncryptUtil {

    /**
     * 生成32的随机盐值
     */
    public static String createSalt(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 加盐加密
     * @param srcPwd    原始密码
     * @param saltValue 盐值
     */
    public static String salt(Object srcPwd, Object saltValue){
        return new Sha256Hash(srcPwd, saltValue, 2).toHex();
    }
}
