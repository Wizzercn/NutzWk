package com.budwk.app.base.utils;

import org.nutz.lang.util.NutMap;

/**
 * Created by wizzer on 2018.09
 */
public class PageUtil {
    public static String getOrder(String key) {
        NutMap map = NutMap.NEW().addv("ascending", "asc").addv("descending", "desc");
        return map.getString(key);
    }
}
