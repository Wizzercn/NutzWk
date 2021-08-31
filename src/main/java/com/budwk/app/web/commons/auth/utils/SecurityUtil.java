package com.budwk.app.web.commons.auth.utils;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import org.nutz.lang.Strings;

/**
 * @author wizzer@qq.com
 */
public class SecurityUtil {
    public static String getUserId() {
        return Strings.sNull(StpUtil.getLoginId());
    }

    public static String getUserLoginname() {
        return Strings.sNull(getSession().get("loginname"));
    }

    public static String getUserUsername() {
        return Strings.sNull(getSession().get("username"));
    }

    public static String getUnitId() {
        return Strings.sNull(getSession().get("unitId"));
    }

    public static String getUnitPath() {
        return Strings.sNull(getSession().get("unitPath"));
    }

    public static SaSession getSession() {
        return StpUtil.getSession(true);
    }
}
