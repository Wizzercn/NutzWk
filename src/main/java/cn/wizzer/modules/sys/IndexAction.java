package cn.wizzer.modules.sys;

import cn.wizzer.common.mvc.filter.PrivateFilter;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

/**
 * Created by Wizzer.cn on 2015/7/4.
 */
@At("/private")
@Filters({@By(type = PrivateFilter.class)})
public class IndexAction {
    @At("/index")
    @Ok("vm:template.private.index")
    @RequiresUser
    public void index(){

    }
}
