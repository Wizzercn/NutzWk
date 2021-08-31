package com.budwk.app.web.commons.auth.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.budwk.app.sys.services.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;

import java.util.List;

/**
 * @author wizzer@qq.com
 */
@IocBean
@Slf4j
public class StpInterfaceImpl implements StpInterface {
    @Inject
    private SysUserService sysUserService;
    @Inject("refer:$ioc")
    protected Ioc ioc;

    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {
        return sysUserService.getPermissionList(Strings.sNull(loginId));
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        return sysUserService.getRoleCodeList(sysUserService.fetch(Strings.sNull(loginId)));
    }
}
