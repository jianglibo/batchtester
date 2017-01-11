package com.jianglibo.batch.util;

import java.util.UUID;

/**
 * @author jianglibo@gmail.com
 *         2015�?8�?12�?
 *
 */
public class UuidUtil {
    
    public static String uuidNoDash() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }
    
    public static String uuidNoDashUpcase() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}
