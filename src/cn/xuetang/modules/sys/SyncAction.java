package cn.xuetang.modules.sys;

import cn.xuetang.common.action.BaseAction;
import cn.xuetang.common.config.Globals;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wizzer on 14-5-21.
 */
@IocBean
@At("/private/sys/sync")
public class SyncAction extends BaseAction {
    @Inject
    protected Dao dao;

    @At("/config")
    @Ok("raw")
    public String config(@Param("key") String key, @Param("type") String type, HttpServletRequest req) {
        String mykey = Strings.sNull(Globals.SYS_CONFIG.get("sync_key"));
        if (mykey.equals(key)) {
            if ("datadict".equals(type)) {
                Globals.InitDataDict(dao);
            } else if ("appinfo".equals(type)) {
                Globals.InitAppInfo(dao);
            } else if ("sysconfig".equals(type)) {
                Globals.InitSysConfig(dao);
            }
        }
        return "sucess";
    }
}
