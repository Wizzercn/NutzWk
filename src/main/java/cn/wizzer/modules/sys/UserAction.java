package cn.wizzer.modules.sys;

import cn.wizzer.common.Message;
import cn.wizzer.common.annotation.SLog;
import cn.wizzer.common.mvc.filter.PrivateFilter;
import cn.wizzer.modules.sys.bean.Sys_unit;
import cn.wizzer.modules.sys.bean.Sys_user;
import cn.wizzer.modules.sys.service.UnitService;
import cn.wizzer.modules.sys.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
@IocBean
@At("/private/sys/user")
@Filters({@By(type = PrivateFilter.class)})
@SLog(tag="用户管理", msg="")
public class UserAction {
    @Inject
    UserService userService;

    @At("")
    @Ok("vm:template.private.sys.user.index")
    @RequiresPermissions("sys:user")
    @SLog(tag="用户列表", msg="访问用户列表")
    public Object index() {
        return "";
    }

    @At
    @Ok("raw")
    @RequiresPermissions("sys:user")
    public String list(@Param("start")int start,@Param("length")int length) {
        System.out.println("start:" + start);
        System.out.println("length:" + length);
        return userService.listPageJson(Sqls.create("select a.username,a.is_online,a.is_locked,b.email,b.nickname from sys_user a,sys_user_profile b where a.id=b.user_id order by a.username asc"),start*length,length);
    }
}
