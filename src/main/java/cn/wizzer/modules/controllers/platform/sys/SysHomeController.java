package cn.wizzer.modules.controllers.platform.sys;

import cn.wizzer.common.base.Globals;
import cn.wizzer.common.filter.PrivateFilter;
import cn.wizzer.modules.models.sys.Sys_menu;
import cn.wizzer.modules.services.sys.SysMenuService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wizzer on 2016/6/23.
 */
@IocBean
@At("/platform/home")
@Filters({@By(type = PrivateFilter.class)})
public class SysHomeController {
    private static final Log log = Logs.get();

    @At("")
    @Ok("beetl:/platform/index.html")
    @RequiresAuthentication
    public void home() {
        //SecurityUtils.getSubject().getSession().setTimeout(1000);
    }

    @At(value={"/", "/index"}, top=true)
    @Ok(">>:/sysadmin")
    public void index() {}
}
