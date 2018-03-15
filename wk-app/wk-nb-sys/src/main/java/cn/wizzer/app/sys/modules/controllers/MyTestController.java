package cn.wizzer.app.sys.modules.controllers;

import cn.wizzer.app.sys.modules.models.Sys_user;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by wizzer on 2018/3/16.
 */
@IocBean
@At("/test")
public class MyTestController {
    @Inject
    private Dao dao;

    @At("/user")
    @Ok("json:full")
    public Object test(){
        return dao.query(Sys_user.class,null);
    }
}
